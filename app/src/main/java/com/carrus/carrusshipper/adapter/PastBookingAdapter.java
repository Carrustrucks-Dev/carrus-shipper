package com.carrus.carrusshipper.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.activity.BookingDetailsActivity;
import com.carrus.carrusshipper.interfaces.OnLoadMoreListener;
import com.carrus.carrusshipper.model.MyBookingDataModel;
import com.carrus.carrusshipper.utils.Utils;

import java.text.ParseException;
import java.util.List;

public class PastBookingAdapter extends RecyclerView.Adapter {
    //    private String[] mDataset;
    private Activity mActivity;
    private List<MyBookingDataModel> myList;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mDatetxtView, mMonthTxtView, mNameTxtView, mCodeTxtView, mAddressTxtView, mTimeTxtView, mStatusTxtView;

        public ViewHolder(View v) {
            super(v);
            mDatetxtView = (TextView) v.findViewById(R.id.dateTxtView);
            mMonthTxtView = (TextView) v.findViewById(R.id.monthTxtView);
            mNameTxtView = (TextView) v.findViewById(R.id.nameTxtView);
            mCodeTxtView = (TextView) v.findViewById(R.id.codeTxtView);
            mAddressTxtView = (TextView) v.findViewById(R.id.addressTxtView);
            mTimeTxtView = (TextView) v.findViewById(R.id.timeTxtView);
            mStatusTxtView = (TextView) v.findViewById(R.id.statusTxtView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PastBookingAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PastBookingAdapter(Activity mActivity, List<MyBookingDataModel> myList, RecyclerView recyclerView) {
        this.mActivity = mActivity;
        this.myList = myList;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
//        // create a new view
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.itemview_booking, parent, false);
//        // set the view's size, margins, paddings and layout parameters
//        ViewHolder vh = new ViewHolder(v);

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.itemview_booking, parent, false);

            vh = new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position
        if (holder instanceof ViewHolder) {
            try {
                ((ViewHolder) holder).mDatetxtView.setText(Utils.getDate(myList.get(position).bookingCreatedAt));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                ((ViewHolder) holder).mMonthTxtView.setText(Utils.getMonth(myList.get(position).bookingCreatedAt));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((ViewHolder) holder).mNameTxtView.setText(myList.get(position).shipper.firstName + " " + myList.get(position).shipper.lastName);
            ((ViewHolder) holder).mCodeTxtView.setText(myList.get(position).truck.truckType.typeTruckName + ", " + myList.get(position).truck.truckNumber);
            ((ViewHolder) holder).mAddressTxtView.setText(myList.get(position).pickUp.city + " to " + myList.get(position).dropOff.city);

            ((ViewHolder) holder).mStatusTxtView.setText(myList.get(position).bookingStatus.replace("_", " "));

            switch (myList.get(position).bookingStatus.toUpperCase()) {
                case "REACHED_DESTINATION":
                case "REACHED_PICKUP_LOCATION":
                    ((ViewHolder) holder).mStatusTxtView.setTextColor(mActivity.getResources().getColor(R.color.tabcolor_dark));
                    break;

                case "ON_GOING":
                case "UP_GOING":
                    ((ViewHolder) holder).mStatusTxtView.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                    break;

                case "CONFIRMED":
                case "ACCEPTED":
                case "ACTIVE":
                    ((ViewHolder) holder).mStatusTxtView.setTextColor(mActivity.getResources().getColor(R.color.green));
                    break;

                case "HALT":
                case "COMPLETED":
                case "PENDING":
                    ((ViewHolder) holder).mStatusTxtView.setTextColor(mActivity.getResources().getColor(R.color.gray_text));
                    break;

                case "CANCELED":
                    ((ViewHolder) holder).mStatusTxtView.setTextColor(mActivity.getResources().getColor(R.color.red));
                    break;

            }

            try {
                ((ViewHolder) holder).mTimeTxtView.setText(Utils.getDay(myList.get(position).pickUp.date) + ", " + myList.get(position).pickUp.time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("value", myList.get(position));
                    Intent intent = new Intent(mActivity, BookingDetailsActivity.class);
                    intent.putExtras(bundle);
                    mActivity.startActivityForResult(intent, 500);

                }
            });
        }else{
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList != null ? myList.size() : 0;
    }
    @Override
    public int getItemViewType(int position) {
        return myList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

}