package com.example.barista.service;

import com.example.barista.data.UserInfo;
import com.example.barista.data.Voucher;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface UserApi {
    @GET("/api/users/info")
    Call<UserInfo> getInfo(@Header("Authorization") String authToken);
}
