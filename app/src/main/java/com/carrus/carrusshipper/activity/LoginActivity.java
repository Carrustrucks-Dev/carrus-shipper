package com.carrus.carrusshipper.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.gcm.DeviceTokenFetcher;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.ConnectionDetector;
import com.carrus.carrusshipper.utils.SessionManager;
import com.carrus.carrusshipper.utils.Utils;
import com.flurry.android.FlurryAgent;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.carrus.carrusshipper.utils.Constants.DEVICE_TYPE;
import static com.carrus.carrusshipper.utils.Constants.MY_FLURRY_APIKEY;
import static com.carrus.carrusshipper.utils.Constants.PASSWORD;
import static com.carrus.carrusshipper.utils.Constants.REMEMBERME;
import static com.carrus.carrusshipper.utils.Constants.SENDER_ID;
import static com.carrus.carrusshipper.utils.Constants.USERNAME;

/**
 * Created by Sunny on 11/5/15 for CarrusShipper.
 */
public class LoginActivity extends BaseActivity {

    private EditText mEmailEdtTxt, mPasswordEdtTxt;
    private SessionManager mSessionManager;
    private ConnectionDetector mConnectionDetector;
    private CheckBox mRememberCheckbox;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSessionManager = new SessionManager(this);
        mConnectionDetector = new ConnectionDetector(this);
        init();
        initializeClickListners();
    }

    private void init() {

        mEmailEdtTxt = (EditText) findViewById(R.id.emailLoginET);
        mPasswordEdtTxt = (EditText) findViewById(R.id.passwdLoginET);
        mRememberCheckbox = (CheckBox) findViewById(R.id.remembermeChkBox);
        mSessionManager = new SessionManager(this);
        getDeviceToken();
        getRememberMe();
    }

    private void initializeClickListners() {
        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSessionManager.getDeviceToken().isEmpty()) {
                    getDeviceToken();
                }
                if (mEmailEdtTxt.getText().toString().trim().isEmpty()) {
                    mEmailEdtTxt.setError(getResources().getString(R.string.email_required));
                    mEmailEdtTxt.requestFocus();
                } else if (mPasswordEdtTxt.getText().toString().trim().isEmpty()) {
                    mPasswordEdtTxt.setError(getResources().getString(R.string.passwd_required));
                    mPasswordEdtTxt.requestFocus();
                } else if (!Utils.isValidEmail(mEmailEdtTxt.getText().toString().trim())) {
                    mEmailEdtTxt.setError(getResources().getString(R.string.validemail_required));
                    mEmailEdtTxt.requestFocus();
                } else {
                    if (mConnectionDetector.isConnectingToInternet())
                        verifyLoggedIn();
                    else
                        Utils.shopAlterDialog(LoginActivity.this, getResources().getString(R.string.nointernetconnection), false);
                }
            }
        });

        findViewById(R.id.forgotPasswdTxtView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, ForgotPasswordActivity.class), 600);
            }
        });

        findViewById(R.id.signUpTxtView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, SignUpActivity.class), 600);
            }
        });
    }

    private void getDeviceToken() {
        new DeviceTokenFetcher(this, new DeviceTokenFetcher.Listener() {
            @Override
            public void onDeviceTokenReceived(String deviceToken) {
                Log.e("Device Token", deviceToken + "");
                mSessionManager.saveDeviceToken(deviceToken);
            }
        }).execute(SENDER_ID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, MY_FLURRY_APIKEY);
        FlurryAgent.onEvent("Login Mode");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    private void verifyLoggedIn() {
        Utils.loading_box(LoginActivity.this);
        RestClient.getApiService().login(mEmailEdtTxt.getText().toString().trim(), mPasswordEdtTxt.getText().toString().trim(), DEVICE_TYPE, Utils.getDeviceName(), mSessionManager.getDeviceToken(), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        if (mRememberCheckbox.isChecked())
                            setRememberMe();
                        else
                            clearRememberMe();

                        JSONObject mDataobject = mObject.getJSONObject("data");
                        mSessionManager.saveUserInfo(mDataobject.getString("accessToken"), mDataobject.getString("userType"), mDataobject.getString("email"), mDataobject.getString("firstName") + " " + mDataobject.getString("lastName"), mDataobject.getString("companyName"), mDataobject.getJSONObject("addressDetails").getString("address"), "", mDataobject.getString("phoneNumber"), mDataobject.getString("rating"), mDataobject.getJSONObject("profilePicture").getString("original"));
                        Toast.makeText(LoginActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivityForResult(new Intent(LoginActivity.this, MainActivity.class), 500);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(LoginActivity.this, Utils.getErrorMsg(error), false);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        Toast.makeText(LoginActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setRememberMe() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor mEditor = prefs.edit();
        mEditor.putBoolean(REMEMBERME, true);
        mEditor.putString(USERNAME, mEmailEdtTxt.getText().toString().trim());
        mEditor.putString(PASSWORD, mPasswordEdtTxt.getText().toString().trim());
        mEditor.commit();

    }

    private void getRememberMe() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (prefs.getBoolean(REMEMBERME, false)) {
            mRememberCheckbox.setChecked(true);
            mEmailEdtTxt.setText(prefs.getString(USERNAME, ""));
            mPasswordEdtTxt.setText(prefs.getString(PASSWORD, ""));
        }
    }

    private void clearRememberMe() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().clear().commit();
    }

}
