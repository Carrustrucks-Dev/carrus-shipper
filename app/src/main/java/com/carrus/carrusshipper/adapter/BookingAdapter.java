package com.carrus.carrusshipper.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.activity.BookingDetailsActivity;
import com.carrus.carrusshipper.model.MyBookingDataModel;
import com.carrus.carrusshipper.model.MyBookingModel;
import com.carrus.carrusshipper.utils.Utils;

import java.text.ParseException;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    //    private String[] mDataset;
    private Activity mActivity;
    private List<MyBookingDataModel> myList;

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
    public BookingAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BookingAdapter(Activity mActivity, List<MyBookingDataModel> myList) {
        this.mActivity = mActivity;
        this.myList = myList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_booking, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position
        try {
            holder.mDatetxtView.setText(Utils.getDate(myList.get(position).bookingCreatedAt));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            holder.mMonthTxtView.setText(Utils.getMonth(myList.get(position).bookingCreatedAt));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mNameTxtView.setText(myList.get(position).shipper.firstName+" "+myList.get(position).shipper.lastName);
        holder.mCodeTxtView.setText(myList.get(position).truck.truckType.typeTruckName+", "+myList.get(position).truck.truckNumber);
        holder.mAddressTxtView.setText(myList.get(position).pickUp.city+" to "+myList.get(position).dropOff.city);
        holder.mStatusTxtView.setText(myList.get(position).bookingStatus);
        try {
            holder.mTimeTxtView.setText(Utils.getDay(myList.get(position).pickUp.date)+", "+myList.get(position).pickUp.time);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("value", myList.get(position));
                Intent intent=new Intent(mActivity, BookingDetailsActivity.class);
                intent.putExtras(bundle);
                mActivity.startActivityForResult(intent, 500);

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList != null ? myList.size() : 0;
    }
}