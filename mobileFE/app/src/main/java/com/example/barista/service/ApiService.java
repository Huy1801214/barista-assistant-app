package com.example.barista.service;

import com.example.barista.request.LoginRequest;
import com.example.barista.request.LoginResponse;
import com.example.barista.request.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("/api/auth/register")
    Call<Void> registerUser(@Body RegisterRequest registerRequest);
}
