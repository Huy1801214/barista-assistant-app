package com.example.barista.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;
import com.example.barista.adpater.VoucherAdapter; // Sửa lại package nếu cần
import com.example.barista.data.Voucher;

import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherManagementActivity extends AppCompatActivity {

    // Views
    private MaterialToolbar toolbar;
    private RecyclerView recyclerViewVouchers;
    private FloatingActionButton fabAddVoucher;
    private ProgressBar progressBar;

    // Logic
    private VoucherAdapter adapter;
    private List<Voucher> voucherList;
    private ApiService apiService;
    private SessionManager sessionManager;

    private static final int ADD_VOUCHER_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_management);

        initViews();
        initLogicComponents();
        setupToolbar();
        setupRecyclerView();
        setupListeners();

        // Thay thế dữ liệu giả bằng cuộc gọi API thật
        loadVouchersFromServer();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewVouchers = findViewById(R.id.recyclerViewVouchers);
        fabAddVoucher = findViewById(R.id.fabAddVoucher);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initLogicComponents() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        voucherList = new ArrayList<>();
        adapter = new VoucherAdapter(this, voucherList);
        recyclerViewVouchers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVouchers.setAdapter(adapter);
    }

    private void setupListeners() {
        fabAddVoucher.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddVoucherActivity.class);
            startActivityForResult(intent, ADD_VOUCHER_REQUEST_CODE);
        });
    }

    // *** ĐÂY LÀ PHẦN THAY ĐỔI QUAN TRỌNG NHẤT ***
    private void loadVouchersFromServer() {
        setLoading(true); // Hiển thị ProgressBar

        String authToken = "Bearer " + sessionManager.fetchAuthToken();

        apiService.getVouchers(authToken).enqueue(new Callback<List<Voucher>>() {
            @Override
            public void onResponse(@NonNull Call<List<Voucher>> call, @NonNull Response<List<Voucher>> response) {
                setLoading(false); // Ẩn ProgressBar

                if (response.isSuccessful() && response.body() != null) {
                    voucherList.clear(); // Xóa dữ liệu cũ
                    voucherList.addAll(response.body()); // Thêm dữ liệu mới từ API
                    adapter.notifyDataSetChanged(); // Cập nhật RecyclerView

                    if (voucherList.isEmpty()) {
                        Toast.makeText(VoucherManagementActivity.this, "Không có voucher nào.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý lỗi, có thể do token hết hạn
                    Toast.makeText(VoucherManagementActivity.this, "Không thể tải dữ liệu. Vui lòng thử lại.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Voucher>> call, @NonNull Throwable t) {
                setLoading(false); // Ẩn ProgressBar
                Toast.makeText(VoucherManagementActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Phương thức này đã được di chuyển ra ngoài setupListeners() để đúng cú pháp Java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_VOUCHER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Người dùng đã tạo voucher thành công, reload danh sách
            Toast.makeText(this, "Đang cập nhật danh sách...", Toast.LENGTH_SHORT).show();
            loadVouchersFromServer(); // Gọi lại API để lấy danh sách mới nhất
        }
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerViewVouchers.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerViewVouchers.setVisibility(View.VISIBLE);
        }
    }
}