package com.example.barista.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.barista.request.LoginResponse;

public class SessionManager {
    private static final String PREF_NAME = "BaristaAppPref";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_ROLE = "user_role";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveLoginSession(String token, LoginResponse.UserInfo userInfo) {
        editor.putString(KEY_AUTH_TOKEN, token);
        if (userInfo != null) {
            editor.putString(KEY_USER_NAME, userInfo.getFullName());
            editor.putString(KEY_USER_ROLE, userInfo.getRole());
        }
        editor.apply();
    }

    public String fetchAuthToken() {
        return pref.getString(KEY_AUTH_TOKEN, null);
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "Guest"); // Trả về "Guest" nếu không tìm thấy
    }

    public String getUserRole() {
        return pref.getString(KEY_USER_ROLE, "Unknown"); // Trả về "Unknown" nếu không tìm thấy
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}
