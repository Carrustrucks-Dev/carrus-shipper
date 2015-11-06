package com.carrus.carrusshipper.utils;


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
     
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
     
    /**
     * Create login session
     * */
    public void saveDeviceToken(String devicetoken){
        // Storing name in pref
        editor.putString(KEY_TOKEN, devicetoken);
         

        // commit changes
        editor.commit();
    }   

    public void saveUserInfo(String accesstoken, String usertype, String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_ACCESSTOKEN, accesstoken);
        editor.putString(KEY_USERTYPE, usertype);
        editor.putString(KEY_EMAIL, email);


        // commit changes
        editor.commit();
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
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
    }
     
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }



    // Get Device Token
    public String getDeviceToken(){
        return pref.getString(KEY_TOKEN, "");
    }

    // Get Access Token
    public String getAccessToken(){
        return pref.getString(KEY_ACCESSTOKEN, "");
    }

}