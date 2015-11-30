package com.carrus.carrusshipper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

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
