package com.carrus.carrusshipper.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.adapter.DividerItemDecoration;
import com.carrus.carrusshipper.adapter.PastBookingAdapter;
import com.carrus.carrusshipper.interfaces.OnLoadMoreListener;
import com.carrus.carrusshipper.model.MyBookingDataModel;
import com.carrus.carrusshipper.model.MyBookingModel;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.CommonNoInternetDialog;
import com.carrus.carrusshipper.utils.ConnectionDetector;
import com.carrus.carrusshipper.utils.Constants;
import com.carrus.carrusshipper.utils.SessionManager;
import com.carrus.carrusshipper.utils.Utils;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.carrus.carrusshipper.utils.Constants.LIMIT;
import static com.carrus.carrusshipper.utils.Constants.MY_FLURRY_APIKEY;
import static com.carrus.carrusshipper.utils.Constants.SORT;

/**
 * Created by Sunny on 10/30/15 for CarrusShipper.
 */
public class PastFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private PastBookingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SessionManager mSessionManager;
    private int skip = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isRefreshView = false;
    private ConnectionDetector mConnectionDetector;
    private List<MyBookingDataModel> bookingList;
    private MyBookingModel mMyBookingModel;
    private TextView mErrorTxtView;
    private LinearLayout mErrorLayout;


    /**
     * Static factory method that takes an int parameter,
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static PastFragment newInstance(int index) {
        PastFragment f = new PastFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.fragment_bookinglist, container, false);
        init(convertView);
        intializeListners();
        return convertView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSessionManager = new SessionManager(getActivity());
        mConnectionDetector = new ConnectionDetector(getActivity());
        if (mConnectionDetector.isConnectingToInternet())
            getPastBookings();
        else {
            Utils.shopAlterDialog(getActivity(), getResources().getString(R.string.nointernetconnection), false);
        }
    }

    private void intializeListners() {
    }

    private void init(View view) {
        mErrorTxtView = (TextView) view.findViewById(R.id.errorTxtView);
        mErrorLayout =(LinearLayout) view.findViewById(R.id.errorLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
//        swipeRefreshLayout.setColorSchemeColors(
//                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshView = true;
                getPastBookings();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Constants.isPastUpdate) {
            Constants.isPastUpdate = false;
            isRefreshView = true;
            getPastBookings();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(getActivity(), MY_FLURRY_APIKEY);
        FlurryAgent.onEvent("Past Booking Mode");
    }

    @Override
    public void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(getActivity());
    }


    private void getPastBookings() {
        mErrorLayout.setVisibility(View.GONE);
        if (isRefreshView) {
            swipeRefreshLayout.setRefreshing(true);
            skip = 0;
            bookingList = null;
        } else {
            if (bookingList == null || bookingList.size() == 0)
                Utils.loading_box(getActivity());
        }

        RestClient.getApiService().getPast(mSessionManager.getAccessToken(), LIMIT + "", skip + "", SORT, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);

                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        Gson gson = new Gson();
                        mMyBookingModel = gson.fromJson(s, MyBookingModel.class);
                        // specify an adapter (see also next example)
                        if (bookingList == null) {
                            bookingList = new ArrayList<MyBookingDataModel>();
                            bookingList.addAll(mMyBookingModel.mData);
                            mAdapter = new PastBookingAdapter(getActivity(), bookingList, mRecyclerView);
                            mRecyclerView.setAdapter(mAdapter);
                            if (mMyBookingModel.mData.size() == LIMIT)
                                setonScrollListener();
                        } else {
                            bookingList.remove(bookingList.size() - 1);
                            mAdapter.notifyItemRemoved(bookingList.size());
                            //add items one by one
                            int start = bookingList.size();
                            int end = start + mMyBookingModel.mData.size();
                            int j = 0;
                            for (int i = start + 1; i <= end; i++) {
                                bookingList.add(mMyBookingModel.mData.get(j));
                                mAdapter.notifyItemInserted(bookingList.size());
                                j++;
                            }
                            mAdapter.setLoaded();
                        }
                        skip = skip + mMyBookingModel.mData.size();
                    } else {
                        if (ApiResponseFlags.Not_Found.getOrdinal() == status) {
                            bookingList.remove(bookingList.size() - 1);
                            mAdapter.notifyItemRemoved(bookingList.size());
                        } else {
                            Utils.shopAlterDialog(getActivity(), mObject.getString("message"), false);
                        }

//                        Toast.makeText(getActivity(), mObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Utils.loading_box_stop();
                isRefreshView = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                swipeRefreshLayout.setRefreshing(false);
                Utils.loading_box_stop();
                try {
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
//                        Utils.shopAlterDialog(getActivity(), getResources().getString(R.string.nointernetconnection), false);
                        noInternetDialog();
                        mAdapter = new PastBookingAdapter(getActivity(), bookingList, mRecyclerView);
                        mRecyclerView.setAdapter(mAdapter);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(getActivity(), Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        //Utils.shopAlterDialog(getActivity(), Utils.getErrorMsg(error), false);
                        mAdapter = new PastBookingAdapter(getActivity(), bookingList, mRecyclerView);
                        mRecyclerView.setAdapter(mAdapter);
                        mErrorTxtView.setText(getResources().getString(R.string.nopastfound));
                        mErrorLayout.setVisibility(View.VISIBLE);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_MORE_RESULT.getOrdinal()) {
                        Toast.makeText(getActivity(), Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                        try {
                            bookingList.remove(bookingList.size() - 1);
                            mAdapter.notifyItemRemoved(bookingList.size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    //Utils.shopAlterDialog(getActivity(), getResources().getString(R.string.nointernetconnection), false);
                    noInternetDialog();
                    mAdapter = new PastBookingAdapter(getActivity(), bookingList, mRecyclerView);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    private void setonScrollListener() {

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                try {
                    bookingList.add(null);
                    mAdapter.notifyItemInserted(bookingList.size() - 1);
                    getPastBookings();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void noInternetDialog() {
        CommonNoInternetDialog.WithActivity(getActivity()).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit), getResources().getString(R.string.callcarrus), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
            @Override
            public void OnOkButtonPressed() {
                isRefreshView = true;
                getPastBookings();
            }

            @Override
            public void OnCancelButtonPressed() {
                getActivity().finish();
            }

            @Override
            public void OnNeutralButtonPressed() {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + Constants.CONTACT_CARRUS));
                    startActivity(callIntent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
