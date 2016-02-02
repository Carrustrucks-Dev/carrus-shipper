package com.carrus.carrusshipper.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.activity.MainActivity;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.CircleTransform;
import com.carrus.carrusshipper.utils.CommonNoInternetDialog;
import com.carrus.carrusshipper.utils.Constants;
import com.carrus.carrusshipper.utils.ImageChooserDialog;
import com.carrus.carrusshipper.utils.SessionManager;
import com.carrus.carrusshipper.utils.Utils;
import com.flurry.android.FlurryAgent;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import static com.carrus.carrusshipper.utils.Constants.MY_FLURRY_APIKEY;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements
        ImageChooserListener, View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private ImageView driverImage;
    private TextView driverName;
    private RatingBar driverRating;
    private TextView cmpanyNameTxtView, emailTxtView, addressTxtView, phoneTxtView, companyTypeTxtView;
    private SessionManager mSessionManager;
    private ImageChooserManager imageChooserManager;
    private int chooserType;
    private String mediaPath;
    private ProgressBar progressBar;


    public ProfileFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_driver_profile, container, false);
        init(v);
        initializeListener(v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSessionManager = new SessionManager(getActivity());
        setData();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("media_path")) {
                mediaPath = savedInstanceState.getString("media_path");
            }
            if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }
        }
    }

    private void init(View v) {


        driverImage = (ImageView) v.findViewById(R.id.driverImage);
        driverName = (TextView) v.findViewById(R.id.driverName);
        driverRating = (RatingBar) v.findViewById(R.id.driverRating);
        cmpanyNameTxtView = (TextView) v.findViewById(R.id.cmpanyNameTxtView);
        emailTxtView = (TextView) v.findViewById(R.id.emailTxtView);
        phoneTxtView = (TextView) v.findViewById(R.id.phoneTxtView);
        addressTxtView = (TextView) v.findViewById(R.id.addressTxtView);
        companyTypeTxtView = (TextView) v.findViewById(R.id.companyTypeTxtView);
        progressBar=(ProgressBar) v.findViewById(R.id.pBar);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.SRC_IN);

    }

    private void initializeListener(View v) {
        v.findViewById(R.id.changeProfileImageBtn).setOnClickListener(this);
        v.findViewById(R.id.imgLayout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isStoragePermissionGranted())
            ImageChooserDialog.With(getActivity()).Show(getResources().getString(R.string.choose_image), new ImageChooserDialog.OnButtonClicked() {
                @Override
                public void onGaleryClicked() {
                    chooseImage();
                }

                @Override
                public void onCameraClicked() {
                    takePicture();
                }
            });
    }

    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    private void setData() {
        if (mSessionManager.getProfilePic() != null && !mSessionManager.getProfilePic().isEmpty())
            Picasso.with(getActivity()).load(mSessionManager.getProfilePic()).placeholder(R.mipmap.icon_placeholder).skipMemoryCache().resize((int)Utils.convertDpToPixel(120, getActivity()), (int)Utils.convertDpToPixel(120, getActivity())).transform(new CircleTransform()).into(driverImage);

        cmpanyNameTxtView.setText(mSessionManager.getCompanyName());
        driverName.setText(mSessionManager.getName());
        emailTxtView.setText(mSessionManager.getEmail());
        addressTxtView.setText(mSessionManager.getAddress());
        phoneTxtView.setText(mSessionManager.getPhone());
        companyTypeTxtView.setText(mSessionManager.getCompanyType());
        driverRating.setRating(Float.valueOf(mSessionManager.getRating()));

    }

    @Override
    public void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(getActivity(), MY_FLURRY_APIKEY);
        FlurryAgent.onEvent("User Profile Mode");
    }

    @Override
    public void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(getActivity());
    }


    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            mediaPath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            mediaPath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("On Activity Result", requestCode + "");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (imageChooserManager == null) {
                imageChooserManager = new ImageChooserManager(this, requestCode, true);
                imageChooserManager.setImageChooserListener(this);
                imageChooserManager.reinitialize(mediaPath);
            }
            imageChooserManager.submit(requestCode, data);
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (image != null) {
                    uploadImage(image
                            .getFileThumbnail());
                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(ProfileFragment.this.getActivity(), reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (chooserType != 0) {
            outState.putInt("chooser_type", chooserType);
        }
        if (mediaPath != null) {
            outState.putString("media_path", mediaPath);
        }
    }


    private void uploadImage(final String path) {
        progressBar.setVisibility(View.VISIBLE);
//        Utils.loading_box(getActivity());
        RestClient.getApiService().uploadProfilePic(mSessionManager.getAccessToken(), new TypedFile("image/*", new File(path)), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);

                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        Log.e("<<>>>>",">> "+mSessionManager.getProfilePic());
                        Toast.makeText(getActivity(), mObject.getString("message"), Toast.LENGTH_SHORT).show();
                        mSessionManager.setProfilePic(mObject.getJSONObject("data").getJSONObject("profilePicture").getString("original"));
                        Log.e("<<>>>>", ">> " + mSessionManager.getProfilePic());
                        driverImage.setBackgroundResource(0);
                        if (mSessionManager.getProfilePic() != null)
                            Picasso.with(getActivity()).load(mSessionManager.getProfilePic()).placeholder(R.mipmap.icon_placeholder).skipMemoryCache().resize((int)Utils.convertDpToPixel(120, getActivity()), (int)Utils.convertDpToPixel(120, getActivity())).transform(new CircleTransform()).into(driverImage, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    Log.i("TAG", "Picasso Success Loading Thumbnail - " + path);
                                }

                                @Override
                                public void onError() {
                                    Log.i("TAG", "Picasso Error Loading Thumbnail Small - " + path);
                                }
                            });
                        ((MainActivity) getActivity()).onRefreshImageView();
                    } else {
                        Utils.shopAlterDialog(getActivity(), mObject.getString("message"), false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
//                Utils.loading_box_stop();
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
//                Utils.loading_box_stop();
                try {
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        noInternetDialog();
//                        Utils.shopAlterDialog(getActivity(), getResources().getString(R.string.nointernetconnection), false);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(getActivity(), Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        Utils.shopAlterDialog(getActivity(), Utils.getErrorMsg(error), false);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_MORE_RESULT.getOrdinal()) {
                        Toast.makeText(getActivity(), Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    noInternetDialog();
//                    Utils.shopAlterDialog(getActivity(), getResources().getString(R.string.nointernetconnection), false);
                }
            }
        });
    }

    private void noInternetDialog() {
        CommonNoInternetDialog.WithActivity(getActivity()).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit), getResources().getString(R.string.callcarrus), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
            @Override
            public void OnOkButtonPressed() {

            }

            @Override
            public void OnCancelButtonPressed() {
                getActivity().finish();
            }

            @Override
            public void OnNeutralButtonPressed() {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + Constants.CONTACT_CARRUS));
                    startActivity(callIntent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
