package com.carrus.carrusshipper.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.activity.BookingDetailsActivity;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    //    private String[] mDataset;
    private Activity mActivity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
//        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
//            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BookingAdapter(Activity mActivity) {
        this.mActivity=mActivity;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivityForResult(new Intent(mActivity, BookingDetailsActivity.class), 500);

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 10;
    }
}