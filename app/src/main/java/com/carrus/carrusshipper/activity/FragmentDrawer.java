package com.carrus.carrusshipper.activity;

/**
 * Created by Ravi on 29/07/15 for CarrusShipper.
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.adapter.NavigationDrawerAdapter;
import com.carrus.carrusshipper.model.NavDrawerItem;
import com.carrus.carrusshipper.utils.CircleTransform;
import com.carrus.carrusshipper.utils.SessionManager;
import com.carrus.carrusshipper.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    //    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private TypedArray icons = null;
    private FragmentDrawerListener drawerListener;
    private SessionManager mSessionManager;
    private ImageView mProfileIV;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    private List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            navItem.setIcon(icons.getResourceId(i, 0));
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);

        // drawer icons
        Resources res = getResources();
        icons = res.obtainTypedArray(R.array.nav_drawer_icons);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mProfileIV = (ImageView) layout.findViewById(R.id.profileIV);
        mSessionManager = new SessionManager(getActivity());
        loadImage();
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        final TextView mCompanyTxtView = (TextView) layout.findViewById(R.id.companyTxtView);
        mCompanyTxtView.setText(mSessionManager.getCompanyName());
        final TextView mUserNameTxtView = (TextView) layout.findViewById(R.id.usernameTxtView);
        mUserNameTxtView.setText(mSessionManager.getName());
        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mDrawerLayout.closeDrawer(containerView);
                drawerListener.onDrawerItemSelected(view, position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        LinearLayout mHeaderLayout = (LinearLayout) layout.findViewById(R.id.nav_header_container);

        mHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onHeaderSelected();
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        return layout;
    }

    public void loadImage() {

        try {
            if (mProfileIV != null && getActivity() != null && mSessionManager.getProfilePic() != null && !mSessionManager.getProfilePic().isEmpty())
                Picasso.with(getActivity()).load(mSessionManager.getProfilePic()).placeholder(R.mipmap.icon_placeholder).skipMemoryCache().resize((int) Utils.convertDpToPixel(70, getActivity()), (int)Utils.convertDpToPixel(70, getActivity())).transform(new CircleTransform()).into(mProfileIV);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
//        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                getActivity().invalidateOptionsMenu();
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                getActivity().invalidateOptionsMenu();
//            }
//
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);
//                toolbar.setAlpha(1 - slideOffset / 2);
//            }
//        };

//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        mDrawerLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mDrawerToggle.syncState();
//            }
//        });

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }


    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);

        void onHeaderSelected();
    }
}
