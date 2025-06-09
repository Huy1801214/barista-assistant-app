package com.example.barista.request;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("tokenType")
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }
    // ... các Getters khác nếu cần
}
