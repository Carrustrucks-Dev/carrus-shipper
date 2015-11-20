package com.carrus.carrusshipper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.adapter.ExpandableListAdapter;
import com.carrus.carrusshipper.model.ExpandableChildItem;
import com.carrus.carrusshipper.model.Header;
import com.carrus.carrusshipper.model.MyBookingDataModel;
import com.carrus.carrusshipper.utils.CircleTransform;
import com.carrus.carrusshipper.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.carrus.carrusshipper.utils.Constants.selectedId;

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
    private List<Header> listDataHeader;
    private HashMap<Header, List<ExpandableChildItem>> listDataChild;
    private ExpandableListAdapter listAdapter;
    private ImageView mProfileIV, locationIV;
    private RelativeLayout topView;

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
        topView=(RelativeLayout) findViewById(R.id.topView);
        mExpandableListView = (ExpandableListView) findViewById(R.id.recyclerview);
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
        paymentBtn = (Button) findViewById(R.id.paymentBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        mProfileIV=(ImageView) findViewById(R.id.profileIV);
        locationIV=(ImageView) findViewById(R.id.locationBtnIV);

        Picasso.with(BookingDetailsActivity.this).load(R.mipmap.icon_placeholder).resize(100,100).transform(new CircleTransform()).into(mProfileIV);

        // Listview Group click listener
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        // Listview Group expanded listener
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {


            }
        });

        // Listview Group collasped listener
        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        // Listview on child click listener
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });

//                                prepareListData();
        listDataHeader = new ArrayList<Header>();
        listDataChild = new HashMap<Header, List<ExpandableChildItem>>();

        // Adding child data
        listDataHeader.add(new Header(getResources().getString(R.string.cargodetails), false));
        listDataHeader.add(new Header(getResources().getString(R.string.notes), false));
        listDataHeader.add(new Header(getResources().getString(R.string.fleetownernotes), false));

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

        findViewById(R.id.callBtnIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mMyBookingDataModel.shipper.phoneNumber));
                startActivity(callIntent);
            }
        });

        locationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent myIntent=new Intent();
//                myIntent.putExtra(selectedId,mMyBookingDataModel.);
            }
        });
    }

    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();
            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    private void setValuesonViews() {

        nameDetailTxtView.setText(mMyBookingDataModel.shipper.firstName + " " + mMyBookingDataModel.shipper.lastName);
        typeDetailTxtView.setText(mMyBookingDataModel.truck.truckType.typeTruckName + ", " + mMyBookingDataModel.truck.truckNumber);
        locationDetailsTxtView.setText(mMyBookingDataModel.pickUp.city + " to " + mMyBookingDataModel.dropOff.city);
        trackDetailsIdTxtView.setText(mMyBookingDataModel.crn);

        statusTxtView.setText(mMyBookingDataModel.bookingStatus);
        if(mMyBookingDataModel.bookingStatus.equalsIgnoreCase("ongoing")){
            topView.setBackgroundColor(getResources().getColor(R.color.blue_ongoing));
            cancelBtn.setVisibility(View.VISIBLE);
            paymentBtn.setVisibility(View.GONE);
            locationIV.setVisibility(View.VISIBLE);
        }else if(mMyBookingDataModel.bookingStatus.equalsIgnoreCase("canceled")){
            topView.setBackgroundColor(getResources().getColor(R.color.red));
            paymentBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
        }else if(mMyBookingDataModel.bookingStatus.equalsIgnoreCase("confirmed")){
            topView.setBackgroundColor(getResources().getColor(R.color.green));
            cancelBtn.setVisibility(View.VISIBLE);
            paymentBtn.setVisibility(View.GONE);
        }else if(mMyBookingDataModel.bookingStatus.equalsIgnoreCase("completed")){
            topView.setBackgroundColor(getResources().getColor(R.color.gray_completed));
            cancelBtn.setVisibility(View.GONE);
            paymentBtn.setVisibility(View.VISIBLE);
        }


        addresPickupTxtView.setText(mMyBookingDataModel.pickUp.address + ", " + mMyBookingDataModel.pickUp.city + ", " + mMyBookingDataModel.pickUp.state + ", " + mMyBookingDataModel.pickUp.zipCode);

        try {
            datePickupTxtView.setText(Utils.getFullDateTime(mMyBookingDataModel.pickUp.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timePickupTxtView.setText(mMyBookingDataModel.pickUp.time);
        addressDropTxtView.setText(mMyBookingDataModel.dropOff.address + ", " + mMyBookingDataModel.dropOff.city + ", " + mMyBookingDataModel.dropOff.state + ", " + mMyBookingDataModel.dropOff.zipCode);
        try {
            dateDropTxtview.setText(Utils.getFullDateTime(mMyBookingDataModel.dropOff.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeDropTxtView.setText(mMyBookingDataModel.dropOff.time);
        paymentModeTxtView.setText(mMyBookingDataModel.paymentMode);
        totalCostTxtView.setText("₹ "+mMyBookingDataModel.acceptPrice);

        // Adding child data
        ArrayList<ExpandableChildItem> cargoDetails = new ArrayList<ExpandableChildItem>();
        cargoDetails.add(new ExpandableChildItem(mMyBookingDataModel.cargo.cargoType.typeCargoName, mMyBookingDataModel.cargo.weight + "", 0));

        // Adding child data
        ArrayList<ExpandableChildItem> notes = new ArrayList<ExpandableChildItem>();
        notes.add(new ExpandableChildItem("", mMyBookingDataModel.jobNote, 1));

        // Adding child data
        ArrayList<ExpandableChildItem> fleetowner = new ArrayList<ExpandableChildItem>();
        fleetowner.add(new ExpandableChildItem("", mMyBookingDataModel.truckerNote, 1));

        listDataChild.put(listDataHeader.get(0), cargoDetails); // Header, Child data
        listDataChild.put(listDataHeader.get(1), notes);
        listDataChild.put(listDataHeader.get(2), fleetowner);

        listAdapter = new ExpandableListAdapter(BookingDetailsActivity.this, listDataHeader, listDataChild);
        mExpandableListView.setAdapter(listAdapter);
        setListViewHeight(mExpandableListView);
//        chnageHieghtListView();
        final ScrollView scrollview = (ScrollView)findViewById(R.id.mainscrollview);

        scrollview.post(new Runnable() {
            public void run() {
                scrollview.scrollTo(0, 0);
            }
        });
    }
}
