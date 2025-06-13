package com.example.barista.request;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("tokenType")
    private String tokenType;
    @SerializedName("userInfo")
    private UserInfo userInfo;

    public static class UserInfo {

        @SerializedName("fullName")
        private String fullName;

        @SerializedName("role")
        private String role;


        // Getters
        public String getFullName() {
            return fullName;
        }

        public String getRole() {
            return role;
        }
    }

    // Getters
    public String getAccessToken() {
        return accessToken;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
