package com.example.barista.service;

import androidx.annotation.Nullable;

import com.example.barista.data.Category;
import com.example.barista.data.Employee;
import com.example.barista.data.ProductItem;
import com.example.barista.data.User;
import com.example.barista.data.Voucher;
import com.example.barista.data.VoucherDto;
import com.example.barista.data.WorkShift;
import com.example.barista.data.WorkShiftHistory;
import com.example.barista.request.LoginRequest;
import com.example.barista.request.LoginResponse;
import com.example.barista.request.RegisterRequest;
import com.example.barista.request.WorkShiftRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService extends ProductApi, VouchersApi, UserApi {
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

    // Lấy danh sách nhân viên của cửa hàng
    @GET("/api/users/staff")
    Call<List<User>> getStaffList(@Header("Authorization") String authToken);

    // Lấy chi tiết một ca làm việc
    @GET("/api/work-shifts/{id}")
    Call<WorkShift> getShiftDetails(@Header("Authorization") String authToken, @Path("id") String shiftId);

    // Tạo ca làm việc mới
    @POST("/api/work-shifts")
    Call<WorkShift> createShift(@Header("Authorization") String authToken, @Body WorkShiftRequest request);

    // Cập nhật ca làm việc
    @PUT("/api/work-shifts/{id}")
    Call<WorkShift> updateShift(@Header("Authorization") String authToken, @Path("id") String shiftId, @Body WorkShiftRequest request);

    @GET("/api/work-shifts/my-shifts")
    Call<List<WorkShift>> getMyShifts(
            @Header("Authorization") String authToken,
            @Query("start") String startDateTime,
            @Query("end") String endDateTime
    );

    @DELETE("/api/work-shifts/{id}")
    Call<Void> cancelShift(
            @Header("Authorization") String authToken,
            @Path("id") String shiftId
    );

    @GET("/api/history/work-shifts")
    Call<List<WorkShiftHistory>> getShiftHistory(
            @Header("Authorization") String authToken,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("employeeId") @Nullable String employeeId // Gửi null nếu không có
    );
    @GET("/api/categories")
    Call<List<Category>> getCategories(@Header("Authorization") String authToken);

    @GET("/api/products")
    Call<List<ProductItem>> getProducts(
            @Header("Authorization") String authToken,
            @Query("categoryId") String categoryId
    );

    @GET("/api/categories")
    Call<List<Category>> getAllCategories(@Header("Authorization") String authToken);

    @POST("/api/categories")
    Call<Category> createCategory(@Header("Authorization") String authToken, @Body Category category);

    @PUT("/api/categories/{id}")
    Call<Category> updateCategory(@Header("Authorization") String authToken, @Path("id") String categoryId, @Body Category category);

    @DELETE("/api/categories/{id}")
    Call<Void> deleteCategory(@Header("Authorization") String authToken, @Path("id") String categoryId);

    @GET("/api/products")
    Call<List<ProductItem>> getProductsByCategory(@Header("Authorization") String authToken, @Query("categoryId") String categoryId);

    @GET("/api/products/{id}")
    Call<ProductItem> getProductById(@Header("Authorization") String authToken, @Path("id") String productId);

    @POST("/api/products")
    Call<ProductItem> createProduct(@Header("Authorization") String authToken, @Body ProductItem product);

    @PUT("/api/products/{id}")
    Call<ProductItem> updateProduct(@Header("Authorization") String authToken, @Path("id") String productId, @Body ProductItem product);

    @DELETE("/api/products/{id}")
    Call<Void> deleteProduct(@Header("Authorization") String authToken, @Path("id") String productId);

    @GET("/api/google/auth/url")
    Call<String> getGoogleAuthUrl(@Header("Authorization") String authToken);
  
    @POST("/api/employees")
    Call<User> createEmployee(@Body User user);

    @PUT("/api/employees/{id}")
    Call<User> updateEmployee(@Path("id") String id, @Body User user);

    @DELETE("/api/employees/{id}")
    Call<Void> deleteEmployee(@Path("id") String id);
}
