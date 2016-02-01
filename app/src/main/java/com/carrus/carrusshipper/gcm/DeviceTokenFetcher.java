package com.carrus.carrusshipper.gcm;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Developer: Rishabh
 * Dated: 03/08/15.
 */
public class DeviceTokenFetcher extends AsyncTask<String, Void, String> {


    // The reference of activity would be used as context
    private Activity activity;

    // The device token fetched would be returned to the Listener
    private Listener listener;

    /**
     * Constructor to initialize the DeviceTokenFetcher
     * with a context and DeviceTokenFetcher.Listener
     * as reference of Activity.
     * <p/>
     * Note: Make sure your activity implements DeviceTokenFetcher.Listener
     *
     * @param activity
     */
    public DeviceTokenFetcher(Activity activity) {
        this(activity, (Listener) activity);
    }

    /**
     * Constructor to initialize the DeviceTokenFetcher
     * with a context and DeviceTokenFetcher.Listener
     * as reference of Fragment.
     * <p/>
     * Note: Make sure your activity implements DeviceTokenFetcher.Listener
     *
     * @param fragment
     */
    public DeviceTokenFetcher(Fragment fragment) {
        this(fragment.getActivity(), (Listener) fragment);
    }

    /**
     * Constructor to initialize the DeviceTokenFetcher
     * with a context as reference of Activity and
     * DeviceTokenFetcher.Listener as reference of
     * listener.
     *
     * @param activity
     */
    public DeviceTokenFetcher(Activity activity, Listener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            if (activity != null && GoogleCloudMessaging.getInstance(activity) != null)
                return GoogleCloudMessaging.getInstance(activity).register(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String deviceToken) {

        if (deviceToken != null)
            listener.onDeviceTokenReceived(deviceToken);
    }

    /**
     * Interfaces the Device token fetcher
     * to the Listening Class.
     * <p/>
     * Developer: Rishabh
     * Dated: 03/08/15.
     */
    public interface Listener {

        /**
         * Override this method to receive a dev
         *
         * @param deviceToken
         */
        void onDeviceTokenReceived(String deviceToken);
    }
}
