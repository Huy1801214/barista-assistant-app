package com.example.barista.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Rất quan trọng:
    // - Dùng http://10.0.2.2:8080/ để kết nối từ máy ảo Android đến localhost trên máy tính.
    // - Thay 192.168.x.x bằng địa chỉ IP của máy tính nếu bạn dùng máy thật.
    private static final String BASE_URL = "http://10.0.2.2:8080/";
//    private static final String BASE_URL = "http://192.168.100.221:8080/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
