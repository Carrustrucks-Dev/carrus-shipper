package com.carrus.carrusshipper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.carrus.carrusshipper.R;

/**
 * Created by Sunny on 11/5/15.
 */
public class LoginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        initializeClickListners();
    }

    private void init(){

    }

    private void initializeClickListners(){
        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, MainActivity.class), 500);
                finish();
            }
        });

        findViewById(R.id.forgotPasswdTxtView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, ForgotPasswordActivity.class), 600);
            }
        });

    }
}
