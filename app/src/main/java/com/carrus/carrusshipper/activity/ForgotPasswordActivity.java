package com.carrus.carrusshipper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrus.carrusshipper.R;

/**
 * Created by Sunny on 11/5/15.
 */
public class ForgotPasswordActivity extends BaseActivity{

    private ImageView mBackButton;
    private TextView headerTxtView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        init();
        initializeClickListners();
    }

    private void init(){
        mBackButton=(ImageView) findViewById(R.id.menu_back_btn);
        mBackButton.setVisibility(View.VISIBLE);
        headerTxtView=(TextView) findViewById(R.id.headerTxtView);
        headerTxtView.setText(getResources().getString(R.string.forgotpasswdlable));
    }

    private void initializeClickListners(){
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
