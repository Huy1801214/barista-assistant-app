package com.example.barista.activity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.barista.R;
import com.example.barista.data.ProductItem;

import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditProductActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private ImageView imageViewProduct;
    private TextInputEditText etProductName, etProductPrice, etImageUrl;
    private TextView tvSelectedCategory;
    private Button btnSave;

    private ApiService apiService;
    private SessionManager sessionManager;
    private String productId;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        initViews();
        initLogic();
        setupToolbar();
        checkModeAndPopulateData();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        imageViewProduct = findViewById(R.id.imageViewProduct);
        etProductName = findViewById(R.id.editTextProductName);
        etProductPrice = findViewById(R.id.editTextProductPrice);
        etImageUrl = findViewById(R.id.editTextImageUrl);
        tvSelectedCategory = findViewById(R.id.textViewSelectedCategory);
        btnSave = findViewById(R.id.buttonSave);
    }

    private void initLogic() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void checkModeAndPopulateData() {
        productId = getIntent().getStringExtra("PRODUCT_ID");
        categoryId = getIntent().getStringExtra("CATEGORY_ID");
        String categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        tvSelectedCategory.setText(categoryName != null ? categoryName : "Không rõ");

        if (productId != null) {
            toolbar.setTitle("Sửa Sản Phẩm");
            loadProductDetails();
        } else {
            toolbar.setTitle("Thêm Sản Phẩm Mới");
        }
    }

    private void loadProductDetails() {
        String token = "Bearer " + sessionManager.fetchAuthToken();
        apiService.getProductById(token, productId).enqueue(new Callback<ProductItem>() {
            @Override
            public void onResponse(@NonNull Call<ProductItem> call, @NonNull Response<ProductItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateForm(response.body());
                } else {
                    Toast.makeText(AddEditProductActivity.this, "Tải chi tiết sản phẩm thất bại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductItem> call, @NonNull Throwable t) {
                Toast.makeText(AddEditProductActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateForm(ProductItem product) {
        etProductName.setText(product.getName());
        if (product.getItemPrice() > 0) {
            etProductPrice.setText(String.valueOf(product.getItemPrice()));
        }
        etImageUrl.setText(product.getThumbnailUrl());
        Glide.with(this).load(product.getThumbnailUrl()).into(imageViewProduct);
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> {
            if (validateInput()) {
                saveProduct();
            }
        });
    }

    private boolean validateInput() {
        // TODO: Thêm các logic kiểm tra hợp lệ khác
        if (etProductName.getText().toString().trim().isEmpty()) {
            etProductName.setError("Tên không được trống");
            return false;
        }
        if (etProductPrice.getText().toString().trim().isEmpty()) {
            etProductPrice.setError("Giá không được trống");
            return false;
        }
        return true;
    }

    private void saveProduct() {
        setLoading(true);
        String token = "Bearer " + sessionManager.fetchAuthToken();

        ProductItem product = new ProductItem();
        product.setName(etProductName.getText().toString().trim());
        try {
            double price = Double.parseDouble(etProductPrice.getText().toString().trim());
            product.setBasePrice(price); // Sửa: Dùng setBasePrice(double)
        } catch (NumberFormatException e) {
            etProductPrice.setError("Giá không hợp lệ");
            setLoading(false);
            return;
        }
        product.setImageURL(etImageUrl.getText().toString().trim());
        product.setCategoryId(this.categoryId);

        Call<ProductItem> apiCall;
        if (productId != null) {
            apiCall = apiService.updateProduct(token, productId, product);
        } else {
            apiCall = apiService.createProduct(token, product);
        }

        apiCall.enqueue(new Callback<ProductItem>() {
            @Override
            public void onResponse(@NonNull Call<ProductItem> call, @NonNull Response<ProductItem> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditProductActivity.this, "Lưu sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AddEditProductActivity.this, "Lưu thất bại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductItem> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(AddEditProductActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        btnSave.setEnabled(!isLoading);
        btnSave.setText(isLoading ? "Đang lưu..." : "Lưu lại");
    }
}
