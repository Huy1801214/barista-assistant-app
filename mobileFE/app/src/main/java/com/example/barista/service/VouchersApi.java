package com.example.barista.service;

import com.example.barista.data.Voucher;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VouchersApi {
    @GET("/api/vouchers")
    Call<List<Voucher>> getVouchers(@Header("Authorization") String authToken);

    @GET("/api/vouchers")
    Call<List<Voucher>> getVoucherByCode(@Header("Authorization") String authToken, @Query("code") String code);
}
