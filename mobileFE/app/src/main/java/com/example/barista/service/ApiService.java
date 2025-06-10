package com.example.barista.service;

import com.example.barista.data.Voucher;
import com.example.barista.data.VoucherDto;
import com.example.barista.data.WorkShift;
import com.example.barista.request.LoginRequest;
import com.example.barista.request.LoginResponse;
import com.example.barista.request.RegisterRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("/api/auth/register")
    Call<Void> registerUser(@Body RegisterRequest registerRequest);

    @POST("/api/vouchers")
    Call<Voucher> createVoucher(@Header("Authorization") String authToken, @Body VoucherDto voucherDto);

    @GET("/api/vouchers")
    Call<List<Voucher>> getVouchers(@Header("Authorization") String authToken);

    // Lấy ca làm việc của cửa hàng (dành cho quản lý)
    @GET("/api/work-shifts")
    Call<List<WorkShift>> getShiftsForStore(
            @Header("Authorization") String authToken,
            @Query("start") String startDateTime, // Gửi lên dạng "yyyy-MM-dd'T'HH:mm:ss"
            @Query("end") String endDateTime
    );

    // Nhân viên tự clock-in
    @POST("/api/work-shifts/{id}/clock-in")
    Call<WorkShift> clockIn(
            @Header("Authorization") String authToken,
            @Path("id") String shiftId
    );

    // Nhân viên tự clock-out
    @POST("/api/work-shifts/{id}/clock-out")
    Call<WorkShift> clockOut(
            @Header("Authorization") String authToken,
            @Path("id") String shiftId
    );
}
