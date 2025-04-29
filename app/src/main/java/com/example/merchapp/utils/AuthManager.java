package com.example.merchapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {
    private static final String PREFS_NAME = "AuthPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_TOKEN = "token";
    private final SharedPreferences prefs;

    public AuthManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveAuthData(String username, String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void clearAuthData() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}