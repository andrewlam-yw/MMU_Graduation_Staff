package com.lamyatweng.mmugraduationstaff;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    static final String PREF_NAME = "MMUGradPref";
    static final String IS_LOGIN = "IsLoggedIn";
    static final String KEY_EMAIL = "email";
    SharedPreferences mPref;
    SharedPreferences.Editor mEditor;
    Context mContext;
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        mContext = context;
        mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String email) {
        mEditor.putBoolean(IS_LOGIN, true);
        mEditor.putString(KEY_EMAIL, email);
        mEditor.commit();
    }

    /**
     * Get stored mSession data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_EMAIL, mPref.getString(KEY_EMAIL, null));
        return user;
    }

    public String getUserEmail() {
        return mPref.getString(KEY_EMAIL, null);
    }

    /**
     * Check user login status
     */
    public boolean isLoggedIn() {
        return mPref.getBoolean(IS_LOGIN, false);
    }

    /**
     * Redirect user to login page user is not logged in
     */
    public void checkLogin() {
        if (!isLoggedIn()) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            // Close all activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Start new Activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    /**
     * Clear mSession detail
     */
    public void logoutUser() {
        // Clearing all data from SharedPreferences
        mEditor.clear().commit();
        // Redirect user to login activity
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
