package com.carrus.carrusshipper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.carrus.carrusshipper.R;

/**
 * Created by Sunny on 10/29/15.
 */
public class SplashActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               startActivityForResult(new Intent(SplashActivity.this, MainActivity.class), 500);
                finish();
            }
        }, 2000);
    }
}
