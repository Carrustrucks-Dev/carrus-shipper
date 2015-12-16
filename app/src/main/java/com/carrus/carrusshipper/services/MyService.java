package com.carrus.carrusshipper.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.fragments.HomeFragment;
import com.carrus.carrusshipper.model.MyBookingModel;
import com.carrus.carrusshipper.retrofit.RestClient;
import com.carrus.carrusshipper.utils.ApiResponseFlags;
import com.carrus.carrusshipper.utils.Constants;
import com.carrus.carrusshipper.utils.SessionManager;
import com.carrus.carrusshipper.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyService extends Service {

    private static String TAG = MyService.class.getSimpleName();
    private MyThread mythread;
    public boolean isRunning = false;
    private String bookingId = null;
    private SessionManager mSessionManager;
    private Context mContext;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mContext = this;
        mythread = new MyThread();
        mSessionManager = new SessionManager(this);
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (isRunning) {
            mythread.interrupt();
            //mythread.stop();
        }
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent != null)
            bookingId = intent.getStringExtra("bookingId");
        Log.d(TAG, "onStart With id " + bookingId);
        if (!isRunning) {
            mythread.start();
            isRunning = true;
        }
    }

    public void readWebPage() {
        Log.d(TAG, "I AM EXCUTED");

        RestClient.getApiService().getSingleOnGoingBookingTrack(mSessionManager.getAccessToken(), bookingId, 100, 0, Constants.SORT, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        Gson gson = new Gson();
                        MyBookingModel mOnGoingShipper = gson.fromJson(s, MyBookingModel.class);

                        Bundle bundle = new Bundle();
                        if (mOnGoingShipper.mData.size() != 0)
                            bundle.putSerializable("data", mOnGoingShipper.mData.get(0));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(HomeFragment.mBroadcastUiAction);
                        broadcastIntent.putExtras(bundle);
                        sendBroadcast(broadcastIntent);

                    } else {
                        Toast.makeText(mContext, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        Toast.makeText(mContext, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        // Utils.shopAlterDialog(mContext, Utils.getErrorMsg(error), true);
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(HomeFragment.mBroadcastAction);
                        broadcastIntent.putExtra("data", Utils.getErrorMsg(error));
                        sendBroadcast(broadcastIntent);
                        //stopService(new Intent(mContext, MyService.class));
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        Toast.makeText(mContext, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(mContext, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class MyThread extends Thread {
        static final long DELAY = 10000;

        @Override
        public void run() {
            while (isRunning) {
                Log.d(TAG, "Running");
                try {
                    readWebPage();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }

    }

}