package com.carrus.carrusshipper.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.fragments.HomeFragment;
import com.carrus.carrusshipper.fragments.MyBookingFragment;
import com.carrus.carrusshipper.fragments.ProfileFragment;
import com.carrus.carrusshipper.model.MyBookingDataModel;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.services.MyService;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.Constants;
import com.carrus.carrusshipper.utils.GMapV2GetRouteDirection;
import com.carrus.carrusshipper.utils.SessionManager;
import com.carrus.carrusshipper.utils.Utils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener, HomeFragment.onSwiperListenerChange {

    private static String TAG = MainActivity.class.getSimpleName();

    private FragmentDrawer drawerFragment;
    private DrawerLayout mDrawerLayout;
    private TextView mHeaderTextView;
    private ImageView mMenuButton, mBackButton;
    private int selectedPos = -1;
    private SessionManager mSessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSessionManager = new SessionManager(this);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout);
        drawerFragment.setDrawerListener(this);

        initializeView();
        initializeClickListners();

        // display the first navigation drawer view on app launch
        displayView(0);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, MyService.class));
    }

    private void initializeView() {
        mHeaderTextView = (TextView) findViewById(R.id.headerTxtView);
        mMenuButton = (ImageView) findViewById(R.id.menu_drawer_btn);
        mMenuButton.setVisibility(View.VISIBLE);
        mBackButton = (ImageView) findViewById(R.id.menu_back_btn);
    }

    private void initializeClickListners() {
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.container_body);
                fragment.addmarkers();
                onStartDrawerSwipe();
            }
        });
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    @Override
    public void onHeaderSelected() {
        selectedPos = -1;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, new ProfileFragment());
        fragmentTransaction.commit();

        // set the toolbar title
//            getSupportActionBar().setTitle(title);
        mHeaderTextView.setText(getString(R.string.myprofile));
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                if (selectedPos != 0) {
                    selectedPos = 0;
                    fragment = new HomeFragment();
                    title = getString(R.string.home);
                }
                break;
            case 1:
                if (selectedPos != 1) {
                    selectedPos = 1;
                    fragment = new MyBookingFragment();
                    title = getString(R.string.my_bookings);
                }
                break;
            case 2:
                if (selectedPos != 2) {
//                    selectedPos = 2;
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + Constants.CONTACT_CARRUS));
                    startActivity(callIntent);
                }
                break;

//            case 3:
//                if (selectedPos != 3) {
//                    selectedPos = 3;
//
//                }
//                break;
            case 3:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.quit)
                        .setMessage(R.string.really_quit)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Stop the activity
//                                MainActivity.this.finish();
                                logout();
                            }

                        })
                        .setNegativeButton(R.string.no, null)
                        .show();

                break;


            default:
                selectedPos = 0;
                fragment = new HomeFragment();
                title = getString(R.string.home);
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
//            getSupportActionBar().setTitle(title);
            mHeaderTextView.setText(title);
        }
    }

    @Override
    public void onStopDrawerSwip() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mMenuButton.setVisibility(View.GONE);
        mBackButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartDrawerSwipe() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mMenuButton.setVisibility(View.VISIBLE);
        mBackButton.setVisibility(View.GONE);
        stopService(new Intent(MainActivity.this, MyService.class));
    }


    private void logout() {
        Utils.loading_box(MainActivity.this);
        RestClient.getApiService().logout(mSessionManager.getAccessToken(), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);

                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        new SessionManager(MainActivity.this).logoutUser();
                    } else {
                        Toast.makeText(MainActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Utils.loading_box_stop();
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    Utils.loading_box_stop();
                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(MainActivity.this, Utils.getErrorMsg(error), true);
                    }
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 500) {
            if (resultCode == Activity.RESULT_OK) {
                displayView(0);
                Bundle bundle = data.getExtras();
                final MyBookingDataModel mMyBookingDataModel =
                        (MyBookingDataModel) bundle.getSerializable("value");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.container_body);
                        if(fragment.mTrackermodel!=null)
                            fragment.mTrackermodel.clear();
                        fragment.mTrackermodel.add(mMyBookingDataModel);
                        if (mMyBookingDataModel.crruentTracking.size() != 0) {
                            onStopDrawerSwip();
                            fragment.getDriectionToDestination(new LatLng(mMyBookingDataModel.crruentTracking.get(0).lat, mMyBookingDataModel.crruentTracking.get(0).longg), mMyBookingDataModel.pickUp.coordinates.pickUpLat + ", " + mMyBookingDataModel.pickUp.coordinates.pickUpLong, mMyBookingDataModel.dropOff.coordinates.dropOffLat + ", " + mMyBookingDataModel.dropOff.coordinates.dropOffLong, GMapV2GetRouteDirection.MODE_DRIVING, 0);
                        }

                    }
                }, 2000);

//                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//                    if (fragment != null)
//                        fragment.onActivityResult(requestCode, resultCode, data);
//                }
//                String stredittext = data.getStringExtra("edittextvalue");
            }
        }

    }
}