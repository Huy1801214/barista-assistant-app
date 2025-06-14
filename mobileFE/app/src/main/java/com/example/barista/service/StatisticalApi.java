package com.example.barista.service;

import com.example.barista.response.StatisticalResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StatisticalApi {
    @GET("/statistical")
    Call<StatisticalResponse> getStatistical(
            @Query("startDate") String startDate,
            @Query("toDate") String endDate
    );
}
