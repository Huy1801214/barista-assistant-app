package com.example.barista.data;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
    public static final String ID = "USER_INFO";
    @SerializedName("storeId")
    private String storeId;


    public UserInfo(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
