package com.carrus.carrusshipper.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.utils.DeviceTokenFetcher;
import com.carrus.carrusshipper.utils.SessionManager;

import static com.carrus.carrusshipper.utils.Constants.SENDER_ID;

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mSessionManager.isLoggedIn()){
                    startActivityForResult(new Intent(SplashActivity.this, MainActivity.class), 500);
                }else{
                    startActivityForResult(new Intent(SplashActivity.this, LoginActivity.class), 500);
                }

                finish();
            }
        }, 2000);
    }

}
