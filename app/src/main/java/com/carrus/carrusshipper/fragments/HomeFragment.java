package com.carrus.carrusshipper.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.activity.MainActivity;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.utils.Constants;
import com.carrus.carrusshipper.utils.GMapV2GetRouteDirection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sunny on 10/29/15.
 */
public class HomeFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    // Google Map
    private GoogleMap googleMap;

    //Markers List
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();

    private MainActivity mainActivity;

    private GMapV2GetRouteDirection v2GetRouteDirection;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v2GetRouteDirection = new GMapV2GetRouteDirection();
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
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//                googleMap.getUiSettings().setAllGesturesEnabled(true);
                googleMap.setTrafficEnabled(true);
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                googleMap.setOnMarkerClickListener(this);
                addmarkers();
            }


        }
    }

    //Add marker function on google map
    public void addmarkers() {
        googleMap.clear();
        for (int i = 0; i < Constants.name.length; i++) {

            LatLng location = new LatLng(Constants.latitude[i],Constants.longitude[i]);

            Marker marker = googleMap.addMarker(new MarkerOptions().position(location)
                    .title(Constants.name[i])
                    .snippet(Constants.name[i])
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_van))
            );

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
                .snippet(marker.getSnippet()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_location_blue)));
        mainActivity.onStopDrawerSwip();
        getDriectionToDestination(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude),marker.getPosition().latitude + ", " + marker.getPosition().longitude, "11.723512, 78.466287", GMapV2GetRouteDirection.MODE_DRIVING);
//        Toast.makeText(getActivity(), marker.getTitle()+", lat> "+marker.getPosition().latitude +"& long> "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
        return false;
    }

    //Change Drawer mode
    public interface onSwiperListenerChange{
        void onStopDrawerSwip();
        void onStartDrawerSwipe();
    }

    //Path Direction Call
    private void getDriectionToDestination(final LatLng currentposition, String start, String end, String mode){
        RestClient.getGoogleApiService().getDriections(start, end, "false", "metric", mode, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                googleMap.clear();
                // convert String into InputStream
                InputStream in = new ByteArrayInputStream(s.getBytes());
                DocumentBuilder builder = null;
                try {
                    builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document doc = builder.parse(in);
                    ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(doc);
                    PolylineOptions rectLine = new PolylineOptions().width(6).color(Color.parseColor("#1F58B9"));

                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    // Adding route on the map
                    googleMap.addPolyline(rectLine);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(currentposition);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_location_blue));
                    googleMap.addMarker(markerOptions);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
