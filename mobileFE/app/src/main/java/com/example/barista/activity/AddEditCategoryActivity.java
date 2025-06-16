package com.example.barista.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.barista.R;
import com.example.barista.data.Category;

import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditCategoryActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextInputEditText editTextCategoryName;
    private Button buttonSave;
    private ApiService apiService;
    private SessionManager sessionManager;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);
        initViews();
        initLogic();
        setupToolbar();
        checkMode();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        buttonSave = findViewById(R.id.buttonSave);
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

    private void checkMode() {
        categoryId = getIntent().getStringExtra("CATEGORY_ID");
        if (categoryId != null) {
            toolbar.setTitle("Sửa Danh mục");
            String categoryName = getIntent().getStringExtra("CATEGORY_NAME");
            editTextCategoryName.setText(categoryName);
        } else {
            toolbar.setTitle("Thêm Danh mục mới");
        }
    }

    private void setupListeners() {
        buttonSave.setOnClickListener(v -> {
            String name = editTextCategoryName.getText().toString().trim();
            if (name.isEmpty()) {
                editTextCategoryName.setError("Tên danh mục không được để trống");
                return;
            }
            saveCategory(name);
        });
    }

    private void saveCategory(String name) {
        setLoading(true);
        String token = "Bearer " + sessionManager.fetchAuthToken();
        Category category = new Category();
        category.setName(name);

        Call<Category> apiCall;
        if (categoryId != null) {
            apiCall = apiService.updateCategory(token, categoryId, category);
        } else {
            apiCall = apiService.createCategory(token, category);
        }

        apiCall.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(@NonNull Call<Category> call, @NonNull Response<Category> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditCategoryActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AddEditCategoryActivity.this, "Lưu thất bại.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Category> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(AddEditCategoryActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        buttonSave.setEnabled(!isLoading);
        buttonSave.setText(isLoading ? "Đang lưu..." : "Lưu lại");
    }
}