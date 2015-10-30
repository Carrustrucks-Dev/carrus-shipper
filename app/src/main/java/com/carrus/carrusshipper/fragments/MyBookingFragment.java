package com.carrus.carrusshipper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carrus.carrusshipper.R;

/**
 * Created by Sunny on 10/30/15.
 */
public class MyBookingFragment  extends Fragment{

    private TextView mUpComingTextView, mPastTextView;
    private int selectedFlag=0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View convertView=inflater.inflate(R.layout.fragment_mybooking, container, false);

        init(convertView);
        initializeClickListners();

        return convertView;
    }


    private void init(View view){
        mUpComingTextView=(TextView) view.findViewById(R.id.upcomingTextView);
        mPastTextView=(TextView) view.findViewById(R.id.pastTextView);
    }

    private void initializeClickListners(){


        mUpComingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedFlag!=0){
                    setSelectionUpcoming();
                }

            }
        });
        mPastTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedFlag!=1){
                    setSeclectionPast();
                }

            }
        });
    }

    private void setSelectionUpcoming(){
        selectedFlag=0;
        mUpComingTextView.setBackgroundColor(getResources().getColor(R.color.tabcolor_dark));
        mPastTextView.setBackgroundColor(getResources().getColor(R.color.tabcolor_light));
    }

    private void setSeclectionPast(){
        selectedFlag=1;
        mUpComingTextView.setBackgroundColor(getResources().getColor(R.color.tabcolor_light));
        mPastTextView.setBackgroundColor(getResources().getColor(R.color.tabcolor_dark));
    }
}
