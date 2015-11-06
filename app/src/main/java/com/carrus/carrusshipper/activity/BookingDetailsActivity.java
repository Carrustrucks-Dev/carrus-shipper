package com.carrus.carrusshipper.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.carrus.carrusshipper.R;

/**
 * Created by Sunny on 11/6/15.
 */
public class BookingDetailsActivity extends BaseActivity {

    private TextView headerTxtView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingdetails);
        init();

        initializeClickListner();

    }

    private void init(){
        headerTxtView=(TextView) findViewById(R.id.headerTxtView);
        headerTxtView.setText(getResources().getString(R.string.bookingdetails));
    }

    private void initializeClickListner(){

    }
}
