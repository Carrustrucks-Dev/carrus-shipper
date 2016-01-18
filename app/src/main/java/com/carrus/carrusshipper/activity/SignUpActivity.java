package com.carrus.carrusshipper.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.carrusshipper.BuildConfig;
import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.model.PartnerModel;
import com.carrus.carrusshipper.model.PartnerShip;
import com.carrus.carrusshipper.model.StateCityInfo;
import com.carrus.carrusshipper.model.StateCityModel;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.CommonNoInternetDialog;
import com.carrus.carrusshipper.utils.Utils;
import com.google.gson.Gson;

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

import static com.carrus.carrusshipper.utils.Constants.COUNTRYNAME;

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
    private List<String> states = new ArrayList<>();
    private List<String> cities = new ArrayList<>();
    private RadioButton mShipperRadioBtn, mBrokerRadioBtn;
    private String USERTYPE = "SHIPPER";
    private EditText mFirstNameET, mLastNameET, mPasswordET, mCnfrmPasswordET, mPhoneNumberET, mCompanyNameET, mAddressET, mPinCodeET, mEmailET;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
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
                    mTypeCompanyTxtView.setText(mPartnerList.get(position).getPartnershipName());
                    mTypeCompanyTxtView.setTextColor(getResources().getColor(android.R.color.black));
                    mTypeCompanyTxtView.setError(null);
                } else {
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
                    mCityTxtView.setText(states.get(position));
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
                if(isFieldFilled()){
                    Toast.makeText(SignUpActivity.this, "Field Filled", Toast.LENGTH_SHORT).show();
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


    private void parseStates() {
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
}
