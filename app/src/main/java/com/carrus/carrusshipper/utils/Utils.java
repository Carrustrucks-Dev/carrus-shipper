package com.carrus.carrusshipper.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.carrus.carrusshipper.R;

/**
 * Created by Sunny on 11/6/15.
 */
public class Utils {

    private static ProgressDialog progressDialog;

    /*
measures height of recyclerview when placed inside scrollview
*/
    public static void getRecyclerViewSize(RecyclerView myListView) {
        RecyclerView.Adapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {

            return;
        }
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getItemCount(); size++) {
            View listItem = myListView.getAdapter().createViewHolder(myListView,0).itemView;
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight
                + (7 * (myListAdapter.getItemCount() - 1));
        myListView.setLayoutParams(params);

        //	Log.i("height of listItem:", String.valueOf(totalHeight));
    }

    /**
     * Loading view
     *
     * @param c   context from where the loading view is in voked
     */

    public static void loading_box(Context c) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(c,
                        android.R.style.Theme_Translucent_NoTitleBar);
                progressDialog.show();
                progressDialog.setCancelable(false);

                progressDialog.setContentView(R.layout.loading_box);

            } else {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * stop loading view
     */
    public static void loading_box_stop() {
        if (progressDialog != null)
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
    }

    /** Returns the consumer friendly device name */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }
    public final static boolean isValidEmail(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
