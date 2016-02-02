package com.carrus.carrusshipper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.CommonNoInternetDialog;
import com.carrus.carrusshipper.utils.ConnectionDetector;
import com.carrus.carrusshipper.utils.Constants;
import com.carrus.carrusshipper.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sunny on 11/5/15 for CarrusShipper.
 */
public class ForgotPasswordActivity extends BaseActivity{

    private ImageView mBackButton;
    private TextView headerTxtView;
    private EditText mEmailEdtTxt;
    private ConnectionDetector mConnectionDetector;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        mConnectionDetector=new ConnectionDetector(this);
        init();
        initializeClickListners();
    }

    private void init(){
        mBackButton=(ImageView) findViewById(R.id.menu_back_btn);
        mBackButton.setVisibility(View.VISIBLE);
        headerTxtView=(TextView) findViewById(R.id.headerTxtView);
        headerTxtView.setText(getResources().getString(R.string.forgotpasswdlable));
        mEmailEdtTxt=(EditText) findViewById(R.id.emailForgotEdtTxt);
    }

    private void initializeClickListners(){
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mEmailEdtTxt.getText().toString().trim().isEmpty()){
                    mEmailEdtTxt.setError(getResources().getString(R.string.email_required));
                    mEmailEdtTxt.requestFocus();
                } else if (!Utils.isValidEmail(mEmailEdtTxt.getText().toString().trim())) {
                    mEmailEdtTxt.setError(getResources().getString(R.string.validemail_required));
                    mEmailEdtTxt.requestFocus();
                }else{
                    if(mConnectionDetector.isConnectingToInternet())
                    forgotPassword();
                    else
                        Utils.shopAlterDialog(ForgotPasswordActivity.this, getResources().getString(R.string.nointernetconnection), false);
                }
            }
        });
    }


    private void forgotPassword(){
        Utils.loading_box(ForgotPasswordActivity.this);

        RestClient.getApiService().forgotPassword(mEmailEdtTxt.getText().toString().trim(), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        Toast.makeText(ForgotPasswordActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        noInternetDialog();
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(ForgotPasswordActivity.this, Utils.getErrorMsg(error), false);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        Toast.makeText(ForgotPasswordActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    noInternetDialog();
                }
            }
        });
    }

    private void noInternetDialog() {
        CommonNoInternetDialog.WithActivity(ForgotPasswordActivity.this).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit), getResources().getString(R.string.callcarrus), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
            @Override
            public void OnOkButtonPressed() {
            }

            @Override
            public void OnCancelButtonPressed() {
                finish();
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
