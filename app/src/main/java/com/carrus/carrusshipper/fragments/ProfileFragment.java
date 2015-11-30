package com.carrus.carrusshipper.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.utils.SessionManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private ImageView driverImage;
    private TextView driverName;
    private RatingBar driverRating;
    private TextView cmpanyNameTxtView, emailTxtView, addressTxtView, phoneTxtView, companyTypeTxtView;
    private SessionManager mSessionManager;

    public ProfileFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_driver_profile, container, false);
        init(v);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSessionManager=new SessionManager(getActivity());
        setData();
    }

    private void init(View v) {

        driverImage = (ImageView) v.findViewById(R.id.driverImage);
        driverName = (TextView) v.findViewById(R.id.driverName);
        driverRating = (RatingBar) v.findViewById(R.id.driverRating);
        cmpanyNameTxtView = (TextView) v.findViewById(R.id.cmpanyNameTxtView);
        emailTxtView = (TextView) v.findViewById(R.id.emailTxtView);
        phoneTxtView = (TextView) v.findViewById(R.id.phoneTxtView);
        addressTxtView = (TextView) v.findViewById(R.id.addressTxtView);
        companyTypeTxtView = (TextView) v.findViewById(R.id.companyTypeTxtView);

    }

    private void setData() {

        cmpanyNameTxtView.setText(mSessionManager.getCompanyName());
        driverName.setText(mSessionManager.getName());
        emailTxtView.setText(mSessionManager.getEmail());
        addressTxtView.setText(mSessionManager.getAddress());
        phoneTxtView.setText(mSessionManager.getPhone());
        companyTypeTxtView.setText(mSessionManager.getCompanyType());
        driverRating.setRating(Float.valueOf(mSessionManager.getRating()));

    }


}
