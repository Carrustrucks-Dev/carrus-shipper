package com.carrus.carrusshipper.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.ConnectionDetector;
import com.carrus.carrusshipper.utils.SessionManager;
import com.carrus.carrusshipper.utils.Utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sunny on 11/19/15.
 */
public class RatingDialogActivity extends BaseActivity {

    private RatingBar ratingStars;
    private EditText feedbackEdtxt;
    private SessionManager sessionManager;
    private float userRating = 0;
    private ConnectionDetector mConnectionDetector;
    private String bookingId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rating);
        sessionManager = new SessionManager(this);
        mConnectionDetector = new ConnectionDetector(this);
        init();
        initializeClickListners();
    }

    private void init() {
        bookingId = getIntent().getStringExtra("bookingid");
        ratingStars = (RatingBar) findViewById(R.id.ratingStars);
        feedbackEdtxt = (EditText) findViewById(R.id.feedbackEdtxt);
    }

    private void initializeClickListners() {

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                userRating = rating;
//                txtRatingValue.setText(String.valueOf(rating));
            }
        });


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
                if (userRating == 0) {
                    Toast.makeText(RatingDialogActivity.this, getResources().getString(R.string.ratingerror), Toast.LENGTH_SHORT).show();
                }
                //Not mendatory
//                else if (feedbackEdtxt.getText().toString().trim().isEmpty()) {
//                    feedbackEdtxt.setError(getResources().getString(R.string.commentrequired));
//                    feedbackEdtxt.requestFocus();
//                }
                else {
                    if (mConnectionDetector.isConnectingToInternet()) {
                        addRating();
                    } else {
                        Toast.makeText(RatingDialogActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    private void addRating() {
        Utils.loading_box(RatingDialogActivity.this);

        RestClient.getApiService().setRating(sessionManager.getAccessToken(), bookingId, String.valueOf(userRating), feedbackEdtxt.getText().toString().trim(), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);

            }

            @Override
            public void failure(RetrofitError error) {
                Utils.loading_box_stop();
                try {
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        Toast.makeText(RatingDialogActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(RatingDialogActivity.this, Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        Toast.makeText(RatingDialogActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(RatingDialogActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
