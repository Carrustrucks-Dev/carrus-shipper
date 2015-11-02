package com.carrus.carrusshipper.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.activity.MainActivity;
import com.carrus.carrusshipper.utils.Constants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Sunny on 10/29/15.
 */
public class HomeFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    // Google Map
    private GoogleMap googleMap;

    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();

    MainActivity mainActivity;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initilizeMap() {
        mainActivity=(MainActivity) getActivity();
        if (googleMap == null) {

            SupportMapFragment fragmentManager = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

            googleMap = fragmentManager.getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getActivity(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            if (googleMap != null) {
                googleMap.setOnMarkerClickListener(this);
                addmarkers();
            }


        }
    }

    public void addmarkers() {
        googleMap.clear();
        for (int i = 0; i < Constants.name.length; i++) {

            LatLng location = new LatLng(Constants.latitude[i],Constants.longitude[i]);

            Marker marker = googleMap.addMarker(new MarkerOptions().position(location)
                    .title(Constants.name[i])
                    .snippet(Constants.name[i]));

            mMarkerArray.add(marker);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude))
                .title(marker.getTitle())
                .snippet(marker.getSnippet()));
        mainActivity.onStopDrawerSwip();
//        Toast.makeText(getActivity(), marker.getTitle()+", lat> "+marker.getPosition().latitude +"& long> "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
        return false;
    }

    public interface onSwiperListenerChange{
        void onStopDrawerSwip();
        void onStartDrawerSwipe();
    }
}
