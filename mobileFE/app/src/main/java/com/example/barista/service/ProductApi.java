package com.example.barista.service;

import com.example.barista.data.ProductItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {
    @GET("/api/products")
    Call<List<ProductItem>> getProducts();

    @GET("api/products")
    Call<ProductItem> getProductById(@Query("id") String productId);
}
