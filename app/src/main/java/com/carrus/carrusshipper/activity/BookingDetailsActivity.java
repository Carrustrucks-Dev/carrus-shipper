package com.carrus.carrusshipper.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.adapter.ExpandableListAdapter;
import com.carrus.carrusshipper.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 11/6/15.
 */
public class BookingDetailsActivity extends BaseActivity {

    private TextView headerTxtView;
    private ImageView mBackBtn;
    private RecyclerView recyclerview;

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
        mBackBtn=(ImageView) findViewById(R.id.menu_back_btn);
        mBackBtn.setVisibility(View.VISIBLE);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Fruits"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Apple"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Orange"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Banana"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Cars"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Audi"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Aston Martin"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "BMW"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Cadillac"));

        ExpandableListAdapter.Item places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Places");
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Kerala"));
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Tamil Nadu"));
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Karnataka"));
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Maharashtra"));

        data.add(places);

        recyclerview.setAdapter(new ExpandableListAdapter(data));
        Utils.getRecyclerViewSize(recyclerview);
    }

    private void initializeClickListner(){
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
