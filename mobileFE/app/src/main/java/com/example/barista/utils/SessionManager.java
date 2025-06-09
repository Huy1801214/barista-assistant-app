package com.example.barista.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "BaristaAppPref";
    private static final String KEY_AUTH_TOKEN = "auth_token";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveAuthToken(String token) {
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    public String fetchAuthToken() {
        return pref.getString(KEY_AUTH_TOKEN, null);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}
