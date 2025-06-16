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
import com.example.barista.adpater.ProductSetupAdapter;
import com.example.barista.data.ProductItem;

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

public class ProductListSetupActivity extends AppCompatActivity implements ProductSetupAdapter.OnProductSetupListener {

    private MaterialToolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddProduct;
    private ProgressBar progressBar;
    private ProductSetupAdapter adapter;
    private List<ProductItem> productList = new ArrayList<>();
    private ApiService apiService;
    private SessionManager sessionManager;
    private String categoryId;
    private String categoryName;

    private final ActivityResultLauncher<Intent> addEditProductLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    loadProductsFromServer();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_setup);

        Intent intent = getIntent();
        categoryId = intent.getStringExtra("CATEGORY_ID");
        categoryName = intent.getStringExtra("CATEGORY_NAME");

        if (categoryId == null || categoryName == null) {
            Toast.makeText(this, "Lỗi: Không có thông tin danh mục.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initViews();
        initLogic();
        setupToolbar();
        setupRecyclerView();
        setupListeners();

        loadProductsFromServer();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerViewProducts);
        fabAddProduct = findViewById(R.id.fabAddProduct);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initLogic() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
    }

    private void setupToolbar() {
        toolbar.setTitle("Sản phẩm trong: " + categoryName);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new ProductSetupAdapter(this, productList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setupListeners() {
        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditProductActivity.class);
            intent.putExtra("CATEGORY_ID", categoryId);
            intent.putExtra("CATEGORY_NAME", categoryName);
            addEditProductLauncher.launch(intent);
        });
    }

    private void loadProductsFromServer() {
        setLoading(true);
        String token = "Bearer " + sessionManager.fetchAuthToken();
        apiService.getProductsByCategory(token, categoryId).enqueue(new Callback<List<ProductItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductItem>> call, @NonNull Response<List<ProductItem>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProductListSetupActivity.this, "Tải sản phẩm thất bại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductItem>> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(ProductListSetupActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProduct(ProductItem product, int position) {
        setLoading(true);
        String token = "Bearer " + sessionManager.fetchAuthToken();
        apiService.deleteProduct(token, product.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    productList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(ProductListSetupActivity.this, "Đã xóa: " + product.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductListSetupActivity.this, "Xóa thất bại.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(ProductListSetupActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClick(ProductItem product) {
        Intent intent = new Intent(this, AddEditProductActivity.class);
        intent.putExtra("PRODUCT_ID", product.getId());
        intent.putExtra("CATEGORY_ID", product.getCategoryId());
        intent.putExtra("CATEGORY_NAME", categoryName);
        addEditProductLauncher.launch(intent);
    }

    @Override
    public void onDeleteClick(ProductItem product, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm '" + product.getName() + "'?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteProduct(product, position))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}