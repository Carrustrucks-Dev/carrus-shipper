package com.carrus.carrusshipper.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.activity.MainActivity;
import com.carrus.carrusshipper.adapter.BookingAdapter;
import com.carrus.carrusshipper.model.MyBookingModel;
import com.carrus.carrusshipper.model.OnGoingShipper;
import com.carrus.carrusshipper.model.TrackingModel;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.services.MyService;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.ConnectionDetector;
import com.carrus.carrusshipper.utils.Constants;
import com.carrus.carrusshipper.utils.GMapV2GetRouteDirection;
import com.carrus.carrusshipper.utils.SessionManager;
import com.carrus.carrusshipper.utils.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.squareup.okhttp.internal.Util;

import org.json.JSONException;
import org.json.JSONObject;
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

    private ArrayList<TrackingModel> mTrackermodel = new ArrayList<>();

    private MainActivity mainActivity;

    private GMapV2GetRouteDirection v2GetRouteDirection;

    private ConnectionDetector mConnectionDetector;

    private SessionManager mSessionManager;

    private OnGoingShipper mOnGoingShipper;

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
        mSessionManager = new SessionManager(getActivity());
        mConnectionDetector = new ConnectionDetector(getActivity());
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
        mainActivity = (MainActivity) getActivity();

        if (!mConnectionDetector.isConnectingToInternet()) {
            Utils.shopAlterDialog(getActivity(), getResources().getString(R.string.nointernetconnection), false);
            return;
        }

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

                getOnGoingBookingTrack();
            }


        }
    }

    //Add marker function on google map
    public void addmarkers() {
        googleMap.clear();
        getActivity().stopService(new Intent(getActivity(), MyService.class));
        mMarkerArray.clear();
        mTrackermodel.clear();
        CameraUpdate center=null;

        for (int i = 0; i < mOnGoingShipper.mData.size(); i++) {

            if (mOnGoingShipper.mData.get(i).tracking.equalsIgnoreCase("yes")) {
                mTrackermodel.add(mOnGoingShipper.mData.get(i));
                LatLng location = new LatLng(mOnGoingShipper.mData.get(i).crruentTracking.get(0).lat, mOnGoingShipper.mData.get(i).crruentTracking.get(0).longg);

                Marker marker = googleMap.addMarker(new MarkerOptions().position(location)
                                .title(mOnGoingShipper.mData.get(i).shipper.firstName)
                                .snippet(mOnGoingShipper.mData.get(i).shipper.firstName)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_van))
                );
                center=
                        CameraUpdateFactory.newLatLng(location);
                mMarkerArray.add(marker);
            }
            if(center!=null)
            googleMap.moveCamera(center);
        }
//        for (int i = 0; i < Constants.name.length; i++) {
//
//            LatLng location = new LatLng(Constants.latitude[i],Constants.longitude[i]);
//
//            Marker marker = googleMap.addMarker(new MarkerOptions().position(location)
//                    .title(Constants.name[i])
//                    .snippet(Constants.name[i])
//                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_van))
//            );
//
//            mMarkerArray.add(marker);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        googleMap.clear();
        mainActivity.onStopDrawerSwip();
        googleMap.addMarker(new MarkerOptions().position(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude))
                .title(marker.getTitle())
                .snippet(marker.getSnippet()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_van)));
        for (int i = 0; i < mMarkerArray.size(); i++) {
            if (marker.equals(mMarkerArray.get(i))) {
                getDriectionToDestination(new LatLng(mTrackermodel.get(i).crruentTracking.get(0).lat, mTrackermodel.get(i).crruentTracking.get(0).longg), mTrackermodel.get(i).pickUp.coordinates.pickUpLat + ", " + mTrackermodel.get(i).pickUp.coordinates.pickUpLong, mTrackermodel.get(i).dropOff.coordinates.dropOffLat + ", " + mTrackermodel.get(i).dropOff.coordinates.dropOffLong, GMapV2GetRouteDirection.MODE_DRIVING);
                break;
            }
        }
//        Toast.makeText(getActivity(), marker.getTitle()+", lat> "+marker.getPosition().latitude +"& long> "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
        return false;
    }

    //Change Drawer mode
    public interface onSwiperListenerChange {
        void onStopDrawerSwip();

        void onStartDrawerSwipe();
    }

    //Path Direction Call
    private void getDriectionToDestination(final LatLng currentposition, final String start, String end, String mode) {
        Utils.loading_box(getActivity());
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
                    Log.v("SIZE > ", "SIZE OF LIST > " + directionPoint.size());
                    PolylineOptions rectLine = new PolylineOptions().width(6).color(Color.parseColor("#1F58B9"));

                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    // Adding route on the map
                    googleMap.addPolyline(rectLine);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(currentposition);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_van));
                    googleMap.addMarker(markerOptions);

                    String[] ar = start.split("[,]");
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(ar[0]), Double.valueOf(ar[1]))).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_location_blue)));

                    CameraUpdate center=
                            CameraUpdateFactory.newLatLng(currentposition);
                    CameraUpdate zoom=CameraUpdateFactory.zoomTo(7);

                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);


                    getActivity().startService(new Intent(getActivity(), MyService.class));
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Utils.loading_box_stop();
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.loading_box_stop();
                try {
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(getActivity(), Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        Toast.makeText(getActivity(), Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getOnGoingBookingTrack() {
        Utils.loading_box(getActivity());

        RestClient.getApiService().getAllOnGoingBookingTrack(mSessionManager.getAccessToken(), 100, 0, Constants.SORT, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        Gson gson = new Gson();
                        mOnGoingShipper = gson.fromJson(s, OnGoingShipper.class);
                        addmarkers();
                    } else {
                        Toast.makeText(getActivity(), mObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.loading_box_stop();
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.loading_box_stop();
                try {
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(getActivity(), Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        Toast.makeText(getActivity(), Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
