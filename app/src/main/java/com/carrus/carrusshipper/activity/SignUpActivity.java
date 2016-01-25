package com.carrus.carrusshipper.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.carrusshipper.BuildConfig;
import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.gcm.DeviceTokenFetcher;
import com.carrus.carrusshipper.model.PartnerModel;
import com.carrus.carrusshipper.model.PartnerShip;
import com.carrus.carrusshipper.model.StateCityInfo;
import com.carrus.carrusshipper.model.StateCityModel;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.CommonNoInternetDialog;
import com.carrus.carrusshipper.utils.SessionManager;
import com.carrus.carrusshipper.utils.Utils;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedString;

import static com.carrus.carrusshipper.utils.Constants.COUNTRYNAME;
import static com.carrus.carrusshipper.utils.Constants.DEVICE_TYPE;
import static com.carrus.carrusshipper.utils.Constants.MY_FLURRY_APIKEY;
import static com.carrus.carrusshipper.utils.Constants.SENDER_ID;

/**
 * Created by Sunny on 1/15/16 for Fleet Owner.
 */
public class SignUpActivity extends BaseActivity {

    private ImageView mBackButton;
    private PartnerModel partnerModel;
    private Spinner mParnterSpinner, stateSpinner, citySpinner;
    private TextView mTypeCompanyTxtView, mStateTxtView, mCityTxtView, mCountryTxtView;
    private List<PartnerShip> mPartnerList;
    private boolean isParntrClick = false;
    private StateCityModel mStateCityModel;
    private List<String> states;
    private List<String> cities;
    private RadioButton mShipperRadioBtn, mBrokerRadioBtn;
    private String USERTYPE = "SHIPPER", PARTNERSHIP_ID=null;
    private EditText mFirstNameET, mLastNameET, mPasswordET, mCnfrmPasswordET, mPhoneNumberET, mCompanyNameET, mAddressET, mPinCodeET, mEmailET;
    private SessionManager sessionManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sessionManager = new SessionManager(this);
        getDeviceToken();
        initView();
        initializeListener();
        parseStates();
        getPartnerShip();
    }

    private void initView() {
        mBackButton = (ImageView) findViewById(R.id.menu_back_btn);
        mBackButton.setVisibility(View.VISIBLE);
        TextView headerTxtView = (TextView) findViewById(R.id.headerTxtView);
        headerTxtView.setText(getResources().getString(R.string.signup_header));
        mParnterSpinner = (Spinner) findViewById(R.id.partnerSpinner);
        stateSpinner = (Spinner) findViewById(R.id.stateSpinner);
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        mTypeCompanyTxtView = (TextView) findViewById(R.id.typeOfCompanyTV);
        mStateTxtView = (TextView) findViewById(R.id.stateTV);
        mCityTxtView = (TextView) findViewById(R.id.cityTxtView);
        mCountryTxtView = (TextView) findViewById(R.id.countryTxtView);

        mShipperRadioBtn = (RadioButton) findViewById(R.id.shipperRadioBtn);
        mBrokerRadioBtn = (RadioButton) findViewById(R.id.brokerRadioBtn);
        mFirstNameET = (EditText) findViewById(R.id.firstnameET);
        mLastNameET = (EditText) findViewById(R.id.lastnameET);
        mPasswordET = (EditText) findViewById(R.id.passwordET);
        mPhoneNumberET = (EditText) findViewById(R.id.phoneNumberET);
        mCompanyNameET = (EditText) findViewById(R.id.companyET);
        mAddressET = (EditText) findViewById(R.id.addressET);
        mPinCodeET = (EditText) findViewById(R.id.zipCodeET);
        mCnfrmPasswordET = (EditText) findViewById(R.id.cnfrmPasswordET);
        mEmailET = (EditText) findViewById(R.id.emailET);
    }

    private void initializeListener() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mParnterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    PARTNERSHIP_ID=mPartnerList.get(position).getId();
                    mTypeCompanyTxtView.setText(mPartnerList.get(position).getPartnershipName());
                    mTypeCompanyTxtView.setTextColor(getResources().getColor(android.R.color.black));
                    mTypeCompanyTxtView.setError(null);
                } else {
                    PARTNERSHIP_ID=null;
                    mTypeCompanyTxtView.setText(getResources().getString(R.string.typecompany));
                    mTypeCompanyTxtView.setTextColor(getResources().getColor(R.color.gray_text));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    mStateTxtView.setText(states.get(position));
                    mStateTxtView.setTextColor(getResources().getColor(android.R.color.black));
                    mStateTxtView.setError(null);
                    mCountryTxtView.setText(COUNTRYNAME);
                    mCountryTxtView.setTextColor(getResources().getColor(android.R.color.black));
                    getCities();
                } else {
                    mStateTxtView.setText(getResources().getString(R.string.state));
                    mStateTxtView.setTextColor(getResources().getColor(R.color.gray_text));
                    mCountryTxtView.setText(getResources().getString(R.string.country));
                    mCountryTxtView.setTextColor(getResources().getColor(R.color.gray_text));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    mCityTxtView.setText(cities.get(position));
                    mCityTxtView.setTextColor(getResources().getColor(android.R.color.black));
                    mCityTxtView.setError(null);
                } else {
                    mCityTxtView.setText(getResources().getString(R.string.city));
                    mCityTxtView.setTextColor(getResources().getColor(R.color.gray_text));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mShipperRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBrokerRadioBtn.setChecked(false);
                    USERTYPE = "SHIPPER";
                }

            }
        });

        mBrokerRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mShipperRadioBtn.setChecked(false);
                    USERTYPE = "BROKER";
                }

            }
        });

        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFieldFilled()) {
//                    Toast.makeText(SignUpActivity.this, "Field Filled", Toast.LENGTH_SHORT).show();
                    genrateOTP();
                }
            }
        });

    }

    public String loadJSONFromAsset() {
        AssetManager assetManager = getResources().getAssets();
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(assetManager.open(
                    "StateCity.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    private void getCities() {
        cities = new ArrayList<>();
        Gson gson = new Gson();
        cities.add("");
        mStateCityModel = gson.fromJson(loadJSONFromAsset(), StateCityModel.class);
        for (StateCityInfo mStateCityInfo : mStateCityModel.data) {
            if (mStateTxtView.getText().toString().trim().equalsIgnoreCase(mStateCityInfo.state))
                cities.add(mStateCityInfo.name);
        }
        Set<String> uniqueList = new HashSet<String>(cities);
        cities = new ArrayList<>(uniqueList); //let GC will doing free memory

        //Sorting
        Collections.sort(cities, new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {

                return obj1.compareTo(obj2);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivity.this,
                android.R.layout.simple_spinner_item, cities);
        citySpinner.setAdapter(adapter);
    }


    private void parseStates() {
        states = new ArrayList<>();
        Gson gson = new Gson();
        states.add("");
        mStateCityModel = gson.fromJson(loadJSONFromAsset(), StateCityModel.class);
        for (StateCityInfo mStateCityInfo : mStateCityModel.data) {
            states.add(mStateCityInfo.state);
        }
        Set<String> uniqueList = new HashSet<String>(states);
        states = new ArrayList<String>(uniqueList); //let GC will doing free memory

        //Sorting
        Collections.sort(states, new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {

                return obj1.compareTo(obj2);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivity.this,
                android.R.layout.simple_spinner_item, states);
        stateSpinner.setAdapter(adapter);
    }

    private void getPartnerShip() {
        Utils.loading_box(SignUpActivity.this);
        RestClient.getApiService().getPartnerShip(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (BuildConfig.DEBUG)
                    Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {

                        Gson gson = new Gson();
                        partnerModel = gson.fromJson(s, PartnerModel.class);
                        mPartnerList = new ArrayList<>();
                        mPartnerList.add(new PartnerShip());
                        for (PartnerShip mPartnerShip : partnerModel.getData()) {
                            if (mPartnerShip.getStatus().equalsIgnoreCase("ACTIVATE")) {
                                mPartnerList.add(mPartnerShip);
                            }
                        }

                        ArrayAdapter<PartnerShip> adapter = new ArrayAdapter<PartnerShip>(SignUpActivity.this,
                                android.R.layout.simple_spinner_item, mPartnerList);
                        mParnterSpinner.setAdapter(adapter);

                    } else {
                        Toast.makeText(SignUpActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.loading_box_stop();
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.loading_box_stop();
                try {
                    if (BuildConfig.DEBUG)
                        Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    noInternetDialog();

                } catch (Exception ex) {
                    noInternetDialog();
                }
            }
        });
    }

    private void noInternetDialog() {
        CommonNoInternetDialog.WithActivity(SignUpActivity.this).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
            @Override
            public void OnOkButtonPressed() {
                getPartnerShip();
            }

            @Override
            public void OnCancelButtonPressed() {
                finish();
            }
        });
    }


    private boolean isFieldFilled() {
        if (sessionManager.getDeviceToken().isEmpty()) {
            getDeviceToken();
        }

        if (checkETEmpty(mFirstNameET))
            return false;
        else if (checkETEmpty(mLastNameET))
            return false;
        else if (checkETEmpty(mEmailET))
            return false;
        else if (checkETEmpty(mCompanyNameET))
            return false;
        else if (mTypeCompanyTxtView.getText().toString().equalsIgnoreCase(getResources().getString(R.string.typecompany))) {
            mTypeCompanyTxtView.setError(getResources().getString(R.string.select_value));
            mTypeCompanyTxtView.requestFocus();
            return false;
        } else if (checkETEmpty(mPasswordET))
            return false;
        else if (checkETEmpty(mCnfrmPasswordET))
            return false;
        else if (checkETEmpty(mPhoneNumberET))
            return false;
        else if (checkETEmpty(mAddressET))
            return false;
        else if (mStateTxtView.getText().toString().equalsIgnoreCase(getResources().getString(R.string.state))) {
            mStateTxtView.setError(getResources().getString(R.string.select_value));
            mStateTxtView.requestFocus();
            return false;
        } else if (mCityTxtView.getText().toString().equalsIgnoreCase(getResources().getString(R.string.city))) {
            mCityTxtView.setError(getResources().getString(R.string.select_value));
            mCityTxtView.requestFocus();
            return false;
        } else if (checkETEmpty(mPinCodeET))
            return false;
        else if (!mPasswordET.getText().toString().trim().equalsIgnoreCase(mCnfrmPasswordET.getText().toString().trim())) {
            mCnfrmPasswordET.setError(getResources().getString(R.string.passwdnotmacth));
            mCnfrmPasswordET.requestFocus();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailET.getText().toString().trim()).matches()) {
            mEmailET.setError(getResources().getString(R.string.validemail_required));
            mEmailET.requestFocus();
            return false;
        }else if(mPhoneNumberET.getText().toString().trim().length()<10){
            mPhoneNumberET.setError(getResources().getString(R.string.phonelimit));
            mPhoneNumberET.requestFocus();
            return false;
        }


        return true;
    }

    private boolean checkETEmpty(EditText mEditText) {
        if (mEditText.getText().toString().trim().isEmpty()) {
            mEditText.setError(getResources().getString(R.string.fieldnotempty));
            mEditText.requestFocus();
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, MY_FLURRY_APIKEY);
        FlurryAgent.onEvent("SignUp Mode");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }


    private void register() {

        JSONArray mJsonArray=new JSONArray();
        mJsonArray.put(PARTNERSHIP_ID);
        Log.v("VALUE", mJsonArray.toString());
        Utils.loading_box(SignUpActivity.this);
        RestClient.getApiService().register(new TypedString(USERTYPE), new TypedString(mEmailET.getText().toString().trim()), new TypedString(mFirstNameET.getText().toString().trim()), new TypedString(mLastNameET.getText().toString().trim()), new TypedString(mPasswordET.getText().toString().trim()), new TypedString(mPhoneNumberET.getText().toString().trim()), new TypedString(mCompanyNameET.getText().toString().trim()), new TypedString(mJsonArray.toString()), new TypedString(mAddressET.getText().toString().trim()), new TypedString(mCityTxtView.getText().toString().trim()), new TypedString(mStateTxtView.getText().toString().trim()), new TypedString(mPinCodeET.getText().toString().trim()), new TypedString(mCountryTxtView.getText().toString().trim()), new TypedString(DEVICE_TYPE), new TypedString(Utils.getDeviceName()), new TypedString(sessionManager.getDeviceToken()), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.Created.getOrdinal() == status) {

                        JSONObject mDataobject = mObject.getJSONObject("data");
                        sessionManager.saveUserInfo(mDataobject.getString("accessToken"), mDataobject.getJSONObject("dataToSet").getString("userType"), mDataobject.getJSONObject("dataToSet").getString("email"), mDataobject.getJSONObject("dataToSet").getString("firstName") + " " + mDataobject.getJSONObject("dataToSet").getString("lastName"), mDataobject.getJSONObject("dataToSet").getString("companyName"), mDataobject.getJSONObject("dataToSet").getJSONObject("addressDetails").getString("address"), "", mDataobject.getJSONObject("dataToSet").getString("phoneNumber"), "0", null);
                        Toast.makeText(SignUpActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(SignUpActivity.this, MainActivity.class);
                        // Closing all the Activities
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Add new Flag to start new Activity
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    } else {
                        Toast.makeText(SignUpActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Utils.loading_box_stop();

            }

            @Override
            public void failure(RetrofitError error) {
                Utils.loading_box_stop();
                try {
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getReason());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDeviceToken() {
        new DeviceTokenFetcher(this, new DeviceTokenFetcher.Listener() {
            @Override
            public void onDeviceTokenReceived(String deviceToken) {
                Log.e("Device Token", deviceToken + "");
                sessionManager.saveDeviceToken(deviceToken);
            }
        }).execute(SENDER_ID);
    }

    private void openOTPDialog(){
        try {

            final Dialog dialog = new Dialog(SignUpActivity.this,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_otp_verifier);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
           final EditText mEditText=(EditText) dialog.findViewById(R.id.otpET);
            Button submitBtn = (Button) dialog.findViewById(R.id.submitBtn);

            submitBtn.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    if(mEditText.getText().toString().trim().isEmpty()){
                        mEditText.setError(getResources().getString(R.string.fieldnotempty));
                        mEditText.requestFocus();
                    }else {
                        dialog.dismiss();
                        verifyOTP(mEditText.getText().toString().trim());
                    }
                }

            });
            ImageView crossBtn=(ImageView) dialog.findViewById(R.id.crossBtn);
            crossBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void genrateOTP(){
        Utils.loading_box(SignUpActivity.this);
        RestClient.getApiService().phoneVerificationGenerate(mPhoneNumberET.getText().toString().trim(), mEmailET.getText().toString().trim(), USERTYPE, "true", "false", new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        FlurryAgent.onEvent("OTP Genrate Mode");
                        openOTPDialog();
                    } else {
                        Toast.makeText(SignUpActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Utils.loading_box_stop();
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.loading_box_stop();
                try {
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getReason());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyOTP(String otp){
        Utils.loading_box(SignUpActivity.this);
        RestClient.getApiService().phoneVerificationVerify(mPhoneNumberET.getText().toString().trim(), otp, USERTYPE, "true", new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.Created.getOrdinal() == status) {
                        FlurryAgent.onEvent("Verify OTP Mode");

                        Toast.makeText(SignUpActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                        register();
                    } else {
                        Toast.makeText(SignUpActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Utils.loading_box_stop();
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.loading_box_stop();
                try {
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getReason());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
