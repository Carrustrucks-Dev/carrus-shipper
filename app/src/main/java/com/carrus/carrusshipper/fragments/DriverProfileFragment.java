package com.carrus.carrusshipper.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.carrus.carrusshipper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DriverProfileFragment extends Fragment {

    private ImageView driverImage;
    private TextView driverName;
    private RatingBar driverRating;
    private TextView driverId, drivingLicense, licenseState, expiresOn, mobileNumber;


    public DriverProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_driver_profile, container, false);
        init(v);
        setDriverData();
        return v;
    }


    private void init(View v) {
        driverImage = (ImageView) v.findViewById(R.id.driverImage);
        driverName = (TextView) v.findViewById(R.id.driverName);
        driverRating = (RatingBar) v.findViewById(R.id.driverRating);
        driverId = (TextView) v.findViewById(R.id.tvDriverId);
        drivingLicense = (TextView) v.findViewById(R.id.tvDrivingLicense);
        licenseState = (TextView) v.findViewById(R.id.tvLicenseState);
        expiresOn = (TextView) v.findViewById(R.id.tvExpiresOn);
        mobileNumber = (TextView) v.findViewById(R.id.tvMobileNumber);
    }

    private void setDriverData() {
//        driverName.setText(sharedPreferences.getString(DRIVAR_NAME, ""));
//        driverId.setText(sharedPreferences.getString(DRIVER_ID, ""));
//        drivingLicense.setText(sharedPreferences.getString(DRIVING_LICENSE, ""));
//        licenseState.setText(sharedPreferences.getString(DL_STATE, ""));
//        expiresOn.setText(sharedPreferences.getString(VALIDITY, ""));
//        mobileNumber.setText(sharedPreferences.getString(DRIVER_PHONENO, ""));
//        driverRating.setRating(Float.valueOf(sharedPreferences.getString(RATING, "0")));
//        try {
//            Calendar cal = Calendar.getInstance();
//            TimeZone tz = cal.getTimeZone();
//            Log.i("Time zone: ", tz.getDisplayName());
//            DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//            f.setTimeZone(TimeZone.getTimeZone("UTC"));
//            Date d = f.parse(sharedPreferences.getString(VALIDITY, ""));
//            DateFormat date = new SimpleDateFormat("MM/yyyy");
//            expiresOn.setText(date.format(d));
//            String path=sharedPreferences.getString(DRIVER_IMAGE, null);
//            if(path.equals(null)){
//
//            }else{
//                Picasso.with(getActivity())
//                        .load(path).fit()
//                        .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).skipMemoryCache()
//                        .placeholder(R.mipmap.icon_placeholder)// optional
//                        .into(driverImage);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }


}
