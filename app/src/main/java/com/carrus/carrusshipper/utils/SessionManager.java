package com.carrus.carrusshipper.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.carrus.carrusshipper.activity.LoginActivity;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "CarrusShipper";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_TOKEN = "deviceToken";

    public static final String KEY_ACCESSTOKEN = "accessToken";
    public static final String KEY_USERTYPE = "usertype";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_RATING = "rating";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_COMPANY_TYPE = "company_type";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void saveDeviceToken(String devicetoken) {
        // Storing name in pref
        editor.putString(KEY_TOKEN, devicetoken);


        // commit changes
        editor.commit();
    }

//        driverId.setText(sharedPreferences.getString(DRIVER_ID, ""));
//        drivingLicense.setText(sharedPreferences.getString(DRIVING_LICENSE, ""));
//        licenseState.setText(sharedPreferences.getString(DL_STATE, ""));
//        expiresOn.setText(sharedPreferences.getString(VALIDITY, ""));
//        mobileNumber.setText(sharedPreferences.getString(DRIVER_PHONENO, ""));
//        driverRating.setRating(Float.valueOf(sharedPreferences.getString(RATING, "0")));

    public void saveUserInfo(String accesstoken, String usertype, String email, String name, String comapnyName, String address, String companyType, String phone, String rating) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_ACCESSTOKEN, accesstoken);
        editor.putString(KEY_USERTYPE, usertype);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_COMPANY_NAME, comapnyName);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_RATING, rating);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_COMPANY_TYPE, companyType);

        // commit changes
        editor.commit();
    }


    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
        ((Activity) _context).finish();
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    // Get Device Token
    public String getDeviceToken() {
        return pref.getString(KEY_TOKEN, "");
    }

    // Get Access Token
    public String getAccessToken() {
        return pref.getString(KEY_ACCESSTOKEN, "");
    }

    public String getUsertype() {
        return pref.getString(KEY_USERTYPE, "");
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, "");
    }

    public String getName() {
        return pref.getString(KEY_NAME, "");
    }

    public String getCompanyName() {
        return pref.getString(KEY_COMPANY_NAME, "");
    }

    public String getPhone() {
        return pref.getString(KEY_PHONE, "");
    }

    public String getRating() {
        return pref.getString(KEY_RATING, "0");
    }

    public String getAddress() {
        return pref.getString(KEY_ADDRESS, "");
    }

    public String getCompanyType() {
        return pref.getString(KEY_COMPANY_TYPE, "Private");
    }
}