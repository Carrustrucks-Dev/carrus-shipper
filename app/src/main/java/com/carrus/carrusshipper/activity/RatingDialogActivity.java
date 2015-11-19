package com.carrus.carrusshipper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.carrus.carrusshipper.R;

/**
 * Created by Sunny on 11/19/15.
 */
public class RatingDialogActivity extends BaseActivity {

    private RatingBar ratingStars;
    private EditText feedbackEdtxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rating);

        init();
        initializeClickListners();
    }

    private void init() {
        ratingStars=(RatingBar) findViewById(R.id.ratingStars);
        feedbackEdtxt=(EditText) findViewById(R.id.feedbackEdtxt);
    }

    private void initializeClickListners() {


        findViewById(R.id.crossIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                finish();
            }
        });


        findViewById(R.id.ratingSubmitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog

            }
        });
    }
}
