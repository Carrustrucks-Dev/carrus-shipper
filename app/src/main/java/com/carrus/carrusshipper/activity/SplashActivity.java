package com.carrus.carrusshipper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.utils.SessionManager;


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

}
