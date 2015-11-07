package com.carrus.carrusshipper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.activity.MainActivity;
import com.carrus.carrusshipper.model.MyBookingDataModel;
import com.carrus.carrusshipper.model.MyBookingModel;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.SessionManager;
import com.carrus.carrusshipper.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.carrus.carrusshipper.utils.Constants.LIMIT;
import static com.carrus.carrusshipper.utils.Constants.SORT;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSelectionUpcoming();
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
        mUpComingTextView.setBackgroundResource(R.drawable.tab_background);
        mPastTextView.setBackgroundResource(R.drawable.tab_past_background_white);
        mUpComingTextView.setTextColor(getResources().getColor(R.color.windowBackground));
        mPastTextView.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        setFragment(new UpComingFragment());

    }

    private void setSeclectionPast(){
        selectedFlag=1;
        mUpComingTextView.setBackgroundResource(R.drawable.tab_upcming_background_white);
        mPastTextView.setBackgroundResource(R.drawable.tab_background);
        mUpComingTextView.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        mPastTextView.setTextColor(getResources().getColor(R.color.windowBackground));
        setFragment(new PastFragment());
    }


    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bookingcontainer_body, fragment);
        fragmentTransaction.commit();
    }

}
