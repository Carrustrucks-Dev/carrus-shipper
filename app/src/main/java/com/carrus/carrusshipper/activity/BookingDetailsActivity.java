package com.carrus.carrusshipper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.adapter.ExpandableListAdapter;
import com.carrus.carrusshipper.model.MyBookingDataModel;
import com.carrus.carrusshipper.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 11/6/15.
 */
public class BookingDetailsActivity extends BaseActivity {

    private TextView headerTxtView;
    private ImageView mBackBtn;
//    private RecyclerView recyclerview;
    private MyBookingDataModel mMyBookingDataModel;
    private TextView nameDetailTxtView, typeDetailTxtView, locationDetailsTxtView, trackDetailsIdTxtView, statusTxtView, addresPickupTxtView, datePickupTxtView, timePickupTxtView, addressDropTxtView, dateDropTxtview, timeDropTxtView, paymentModeTxtView, totalCostTxtView;
    private Button paymentBtn, cancelBtn;
    private ExpandableListView mExpandableListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingdetails);
        init();
        initializeClickListner();

    }

    private void init() {
        headerTxtView = (TextView) findViewById(R.id.headerTxtView);
        headerTxtView.setText(getResources().getString(R.string.bookingdetails));
        mBackBtn = (ImageView) findViewById(R.id.menu_back_btn);
        mBackBtn.setVisibility(View.VISIBLE);
//        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
//        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mExpandableListView=(ExpandableListView) findViewById(R.id.recyclerview);
        nameDetailTxtView = (TextView) findViewById(R.id.nameDetailTxtView);
        typeDetailTxtView = (TextView) findViewById(R.id.typeDetailTxtView);
        locationDetailsTxtView = (TextView) findViewById(R.id.locationDetailsTxtView);
        trackDetailsIdTxtView = (TextView) findViewById(R.id.trackDetailsIdTxtView);
        statusTxtView = (TextView) findViewById(R.id.statusTxtView);
        addresPickupTxtView = (TextView) findViewById(R.id.addresPickupTxtView);
        datePickupTxtView = (TextView) findViewById(R.id.datePickupTxtView);
        timePickupTxtView = (TextView) findViewById(R.id.timePickupTxtView);
        addressDropTxtView = (TextView) findViewById(R.id.addressDropTxtView);
        dateDropTxtview = (TextView) findViewById(R.id.dateDropTxtview);
        timeDropTxtView = (TextView) findViewById(R.id.timeDropTxtView);
        paymentModeTxtView = (TextView) findViewById(R.id.paymentModeTxtView);
        totalCostTxtView = (TextView) findViewById(R.id.totalCostTxtView);
        paymentBtn=(Button) findViewById(R.id.paymentBtn);
        cancelBtn=(Button) findViewById(R.id.cancelBtn);


        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 1, getResources().getString(R.string.cargodetails)));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 2, getResources().getString(R.string.notes)));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, 2, "Audi"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, 2, "Aston Martin"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, 2, "BMW"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, 2, "Cadillac"));

        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 2, getResources().getString(R.string.fleetownernotes)));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, 2, "Kerala"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, 2, "Tamil Nadu"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, 2, "Karnataka"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, 2, "Maharashtra"));

//        recyclerview.setAdapter(new ExpandableListAdapter(BookingDetailsActivity.this, data));
//        chnageHieghtListView();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        mMyBookingDataModel =
                (MyBookingDataModel) bundle.getSerializable("value");

        setValuesonViews();
    }

    private void initializeClickListner() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

//    public void chnageHieghtListView() {
//        Utils.getRecyclerViewSize(recyclerview);
//    }



    private void setValuesonViews() {

        nameDetailTxtView.setText(mMyBookingDataModel.shipper.firstName+" "+mMyBookingDataModel.shipper.lastName);
        typeDetailTxtView.setText(mMyBookingDataModel.truck.truckType.typeTruckName+", "+mMyBookingDataModel.truck.truckNumber);
        locationDetailsTxtView.setText(mMyBookingDataModel.pickUp.city+" to "+mMyBookingDataModel.dropOff.city);
//        trackDetailsIdTxtView
        statusTxtView.setText(mMyBookingDataModel.bookingStatus);
        addresPickupTxtView.setText(mMyBookingDataModel.pickUp.address+", "+mMyBookingDataModel.pickUp.city+", "+mMyBookingDataModel.pickUp.state+", "+mMyBookingDataModel.pickUp.zipCode);
        try {
            datePickupTxtView.setText(Utils.getFullDateTime(mMyBookingDataModel.pickUp.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timePickupTxtView.setText(mMyBookingDataModel.pickUp.time);
                        addressDropTxtView.setText(mMyBookingDataModel.dropOff.address+", "+mMyBookingDataModel.dropOff.city+", "+mMyBookingDataModel.dropOff.state+", "+mMyBookingDataModel.dropOff.zipCode);
        try {
            dateDropTxtview.setText(Utils.getFullDateTime(mMyBookingDataModel.dropOff.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeDropTxtView.setText(mMyBookingDataModel.dropOff.time);
                                paymentModeTxtView.setText(mMyBookingDataModel.paymentMode);
                                        totalCostTxtView.setText(mMyBookingDataModel.acceptPrice);
    }
}
