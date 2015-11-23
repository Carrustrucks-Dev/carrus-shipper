package com.carrus.carrusshipper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carrus.carrusshipper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 10/30/15.
 */
public class MyBookingFragment  extends Fragment{

    private TextView mUpComingTextView, mPastTextView;
    private int selectedFlag=0;
    private List<Fragment> myFragmentList=new ArrayList<>();

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
        myFragmentList.add(UpComingFragment.newInstance(0));
        myFragmentList.add(PastFragment.newInstance(1));

        setSelectionUpcoming(0);
    }

    private void init(View view){
        mUpComingTextView=(TextView) view.findViewById(R.id.upcomingTextView);
        mPastTextView=(TextView) view.findViewById(R.id.pastTextView);

//        Intent i = new Intent(getActivity(), RatingDialogActivity.class);
//        i.putExtra("bookingid", bookingid);
//        startActivity(i);
    }

    private void initializeClickListners(){


        mUpComingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedFlag!=0){
                    setSelectionUpcoming(0);
                }

            }
        });
        mPastTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedFlag!=1){
                    setSeclectionPast(1);
                }

            }
        });
    }

    private void setSelectionUpcoming(int button_id){
        selectedFlag=0;
        mUpComingTextView.setBackgroundResource(R.drawable.tab_background);
        mPastTextView.setBackgroundResource(R.drawable.tab_past_background_white);
        mUpComingTextView.setTextColor(getResources().getColor(R.color.windowBackground));
        mPastTextView.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        setFragment(myFragmentList.get(0),button_id);

    }

    private void setSeclectionPast(int button_id){

        selectedFlag=1;
        mUpComingTextView.setBackgroundResource(R.drawable.tab_upcming_background_white);
        mPastTextView.setBackgroundResource(R.drawable.tab_background);
        mUpComingTextView.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        mPastTextView.setTextColor(getResources().getColor(R.color.windowBackground));
        setFragment(myFragmentList.get(1),button_id);

    }


    private void setFragment(Fragment fragment, int button_id){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // If fragment doesn't exist yet, create one
        if (fragment.isAdded())
        {
            if(fragment instanceof UpComingFragment) {
                fragmentTransaction.hide(myFragmentList.get(1));
            }else if(fragment instanceof PastFragment){
                fragmentTransaction.hide(myFragmentList.get(0));
            }
            fragmentTransaction.show(fragment);
        }

        else { // re-use the old fragment
            if(fragment instanceof PastFragment){
                fragmentTransaction.hide(myFragmentList.get(0));
            }
            fragmentTransaction.add(R.id.bookingcontainer_body, fragment, button_id + "stack_item");
        }

        fragmentTransaction.commit();

    }

}
