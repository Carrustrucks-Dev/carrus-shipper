package com.carrus.carrusshipper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.carrus.carrusshipper.BuildConfig;
import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.SessionManager;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.carrus.carrusshipper.utils.Constants.APPTYPE;


/**
 * Created by Sunny on 10/29/15.
 */
public class SplashActivity extends BaseActivity {

    private SessionManager mSessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSessionManager = new SessionManager(this);
        checkVersion();
        //force crash for cashlytics.....for testing purpose
//        int i = 1 / 0;


//        int density = getResources().getDisplayMetrics().densityDpi;
//        switch (density) {
//            case DisplayMetrics.DENSITY_LOW:
//                Toast.makeText(this, "LDPI", Toast.LENGTH_SHORT).show();
//                break;
//            case DisplayMetrics.DENSITY_MEDIUM:
//                Toast.makeText(this, "MDPI", Toast.LENGTH_SHORT).show();
//                break;
//            case DisplayMetrics.DENSITY_HIGH:
//                Toast.makeText(this, "HDPI", Toast.LENGTH_SHORT).show();
//                break;
//            case DisplayMetrics.DENSITY_XHIGH:
//                Toast.makeText(this, "XHDPI", Toast.LENGTH_SHORT).show();
//                break;
//        }

    }

    private void checkVersion() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);
        RestClient.getApiService().getAppVersion(APPTYPE, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (BuildConfig.DEBUG)
                    Log.v("SPLASH SCREEN", "Response > " + s);
                try {
                    JSONObject mObject = new JSONObject(s);
                    int status = mObject.getInt("statusCode");
                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        final JSONObject data = mObject.getJSONObject("data");
                        PackageManager manager = SplashActivity.this.getPackageManager();
                        final PackageInfo info = manager.getPackageInfo(SplashActivity.this.getPackageName(), 0);
                        JSONObject mAndroidVersion = data.getJSONObject("ANDROID");
                        if (mAndroidVersion.has("criticalVersion")) {
                            if (Integer.parseInt(mAndroidVersion.getString("criticalVersion")) > info.versionCode) {
                                alertDialog.setMessage(getString(R.string.critical_update_message));
                                alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        goToAppStore();
                                    }
                                });
                                alertDialog.show();
                            } else {
                                goToNextActivity();
                            }

                        } else if (Integer.parseInt(mAndroidVersion.getString("version")) > info.versionCode) {
                            alertDialog.setMessage(getString(R.string.update_message));
                            alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    goToAppStore();
                                }
                            });
                            alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    goToNextActivity();
                                }
                            });
                            alertDialog.show();
                        } else {
                            goToNextActivity();
                        }
                    }
                } catch (Exception ex) {
                    goToNextActivity();
                    ex.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                goToNextActivity();
            }
        });
    }

    private void goToNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSessionManager.isLoggedIn()) {
                    startActivityForResult(new Intent(SplashActivity.this, MainActivity.class), 500);
                } else {
                    startActivityForResult(new Intent(SplashActivity.this, LoginActivity.class), 500);
                }
                finish();
            }
        }, 2000);
    }

    private void goToAppStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.carrus.carrusshipper"));
        try {
            startActivity(intent);
        } catch (Exception e) {
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.carrus.carrusshipper"));
        }
    }
}
