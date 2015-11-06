package com.carrus.carrusshipper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.DeviceTokenFetcher;
import com.carrus.carrusshipper.utils.SessionManager;
import com.carrus.carrusshipper.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.carrus.carrusshipper.utils.Constants.DEVICE_TYPE;
import static com.carrus.carrusshipper.utils.Constants.SENDER_ID;

/**
 * Created by Sunny on 11/5/15.
 */
public class LoginActivity extends BaseActivity {

    private EditText mEmailidEdtTxt, mPasswordEdtTxt;
    private SessionManager mSessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSessionManager = new SessionManager(this);
        init();
        initializeClickListners();
    }

    private void init() {
        mEmailidEdtTxt = (EditText) findViewById(R.id.emailLoginET);
        mPasswordEdtTxt = (EditText) findViewById(R.id.passwdLoginET);
        mSessionManager = new SessionManager(this);
        new DeviceTokenFetcher(this, new DeviceTokenFetcher.Listener() {
            @Override
            public void onDeviceTokenReceived(String deviceToken) {
                Log.e("Device Token", deviceToken);
                mSessionManager.saveDeviceToken(deviceToken);
            }
        }).execute(SENDER_ID);
    }

    private void initializeClickListners() {
        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEmailidEdtTxt.getText().toString().trim().isEmpty()) {
                    mEmailidEdtTxt.setError(getResources().getString(R.string.email_required));
                    mEmailidEdtTxt.requestFocus();
                } else if (mPasswordEdtTxt.getText().toString().trim().isEmpty()) {
                    mPasswordEdtTxt.setError(getResources().getString(R.string.passwd_required));
                    mPasswordEdtTxt.requestFocus();
                } else if (!Utils.isValidEmail(mEmailidEdtTxt.getText().toString().trim())) {
                    mEmailidEdtTxt.setError(getResources().getString(R.string.validemail_required));
                    mEmailidEdtTxt.requestFocus();
                } else {
                    verifyLoggedIn();
                }
            }
        });

        findViewById(R.id.forgotPasswdTxtView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, ForgotPasswordActivity.class), 600);
            }
        });
    }

    private void verifyLoggedIn() {
        Utils.loading_box(LoginActivity.this);
        RestClient.getApiService().login(mEmailidEdtTxt.getText().toString().trim(), mPasswordEdtTxt.getText().toString().trim(), DEVICE_TYPE, Utils.getDeviceName(), mSessionManager.getDeviceToken(), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {

                        JSONObject mDataobject = mObject.getJSONObject("data");

                        mSessionManager.saveUserInfo(mDataobject.getString("accessToken"), mDataobject.getString("userType"), mDataobject.getString("email"));
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
            }
        });
    }

}
