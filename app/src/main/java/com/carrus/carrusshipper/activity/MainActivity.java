package com.carrus.carrusshipper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.fragments.HomeFragment;
import com.carrus.carrusshipper.fragments.MyBookingFragment;

import org.w3c.dom.Text;


public class MainActivity extends FragmentActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private FragmentDrawer drawerFragment;
    private DrawerLayout mDrawerLayout;
    private TextView mHeaderTextView;

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
    }

    private void initializeClickListners(){
        findViewById(R.id.menu_drawer_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
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
                fragment = new HomeFragment();
                title = getString(R.string.home);
                break;
            case 1:
                fragment = new MyBookingFragment();
                title = getString(R.string.my_bookings);
                break;
            case 2:
//                fragment = new MessagesFragment();
//                title = getString(R.string.title_messages);
                break;
            default:
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
}