package com.example.barista.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.barista.R;
import com.example.barista.adpater.MenuCategoryAdapter;
import com.example.barista.data.Category;

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

public class MenuSetupActivity extends AppCompatActivity implements MenuCategoryAdapter.OnCategorySetupListener {

    private MaterialToolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddCategory;
    private ProgressBar progressBar;
    private MenuCategoryAdapter adapter;
    private List<Category> categoryList = new ArrayList<>();
    private ApiService apiService;
    private SessionManager sessionManager;

    private final ActivityResultLauncher<Intent> addEditCategoryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    loadCategoriesFromServer();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_setup);
        initViews();
        initLogic();
        setupToolbar();
        setupRecyclerView();
        setupListeners();
        loadCategoriesFromServer();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerViewMenuCategories);
        fabAddCategory = findViewById(R.id.fabAddCategory);
        progressBar = findViewById(R.id.progressBar);
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

    private void setupRecyclerView() {
        adapter = new MenuCategoryAdapter(categoryList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setupListeners() {
        fabAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditCategoryActivity.class);
            addEditCategoryLauncher.launch(intent);
        });
    }

    private void loadCategoriesFromServer() {
        setLoading(true);
        String token = "Bearer " + sessionManager.fetchAuthToken();
        apiService.getAllCategories(token).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MenuSetupActivity.this, "Tải danh mục thất bại.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(MenuSetupActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCategory(Category category, int position) {
        setLoading(true);
        String token = "Bearer " + sessionManager.fetchAuthToken();
        apiService.deleteCategory(token, category.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    categoryList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(MenuSetupActivity.this, "Đã xóa: " + category.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MenuSetupActivity.this, "Xóa thất bại.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(MenuSetupActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(Category category) {
        // Màn hình quản lý sản phẩm chưa được tạo, tạm thời comment
        // Intent intent = new Intent(this, ProductListSetupActivity.class);
        // intent.putExtra("CATEGORY_ID", category.getId());
        // intent.putExtra("CATEGORY_NAME", category.getName());
        // startActivity(intent);
        Toast.makeText(this, "Mở sản phẩm của: " + category.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditClick(Category category) {
        Intent intent = new Intent(this, AddEditCategoryActivity.class);
        intent.putExtra("CATEGORY_ID", category.getId());
        intent.putExtra("CATEGORY_NAME", category.getName());
        addEditCategoryLauncher.launch(intent);
    }

    @Override
    public void onDeleteClick(Category category, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa danh mục '" + category.getName() + "' không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteCategory(category, position))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}