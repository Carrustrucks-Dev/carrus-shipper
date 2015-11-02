package com.carrus.carrusshipper.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.fragments.HomeFragment;
import com.carrus.carrusshipper.fragments.MyBookingFragment;

import org.w3c.dom.Text;


public class MainActivity extends FragmentActivity implements FragmentDrawer.FragmentDrawerListener, HomeFragment.onSwiperListenerChange {

    private static String TAG = MainActivity.class.getSimpleName();

    private FragmentDrawer drawerFragment;
    private DrawerLayout mDrawerLayout;
    private TextView mHeaderTextView;
    private ImageView mMenuButton, mBackButton;
    private int selectedPos=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout);
        drawerFragment.setDrawerListener(this);

        initializeView();
        initializeClickListners();

        // display the first navigation drawer view on app launch
        displayView(0);


    }

    private void initializeView(){
        mHeaderTextView=(TextView) findViewById(R.id.headerTxtView);
        mMenuButton=(ImageView)findViewById(R.id.menu_drawer_btn);
        mBackButton=(ImageView)findViewById(R.id.menu_back_btn);
    }

    private void initializeClickListners(){
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

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                if(selectedPos!=0) {
                    selectedPos = 0;
                    fragment = new HomeFragment();
                    title = getString(R.string.home);
                }
                break;
            case 1:
                if(selectedPos!=1) {
                    selectedPos = 1;
                    fragment = new MyBookingFragment();
                    title = getString(R.string.my_bookings);
                }
                break;
            case 2:
                if(selectedPos!=2) {
                    selectedPos = 2;

                }
                break;

            case 3:
                if(selectedPos!=3) {
                    selectedPos = 3;

                }
                break;
            case 4:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.quit)
                        .setMessage(R.string.really_quit)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Stop the activity
                                MainActivity.this.finish();
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
    }
}