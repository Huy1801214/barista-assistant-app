package com.example.barista.service;


import com.example.barista.request.OrderRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OrderApi {
//    public static boolean createOrder(OrderRequest orderRequest) throws IOException {
//        String json = orderRequest.decodeToJson();
//        URL url = new URL(Api.URL + "/order");
//
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setDoOutput(true);
//
//        try (var os = connection.getOutputStream()) {
//            os.write(json.getBytes());
//        }
//
//        int responseCode = connection.getResponseCode();
//
//        return responseCode == HttpURLConnection.HTTP_OK;
//    }

    @POST("/order")
    Call<Void> createOrder(@Header("Authorization") String authToken, @Body OrderRequest orderRequest);
}
