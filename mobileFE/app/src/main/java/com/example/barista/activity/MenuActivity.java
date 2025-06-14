package com.example.barista.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;
import com.example.barista.adpater.CategoryAdapter;
import com.example.barista.adpater.MenuProductListAdapter;
import com.example.barista.data.Category;
import com.example.barista.data.ProductItem;
import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    // Views
    private RecyclerView recyclerCategories, recyclerProducts;
    private ImageView imageViewBack, imageViewAction3;
    private LinearLayout layoutVoucher;
    // ... Khai báo các view khác nếu cần

    // Logic
    private CategoryAdapter categoryAdapter;
    private MenuProductListAdapter productAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<ProductItem> productList = new ArrayList<>();
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        initViews();
        initLogic();
        setupRecyclerViews();
        setupListeners();

        // Bắt đầu quá trình tải dữ liệu
        loadCategories();
    }

    private void initViews() {
        recyclerCategories = findViewById(R.id.recyclerViewCategories);
        recyclerProducts = findViewById(R.id.recyclerViewProducts);
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewAction3 = findViewById(R.id.imageViewAction3);
        layoutVoucher = findViewById(R.id.layoutVoucher);
        // ... Ánh xạ các view khác từ layout
    }

    private void initLogic() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
    }

    private void setupRecyclerViews() {
        // Cài đặt RecyclerView cho danh mục
        categoryAdapter = new CategoryAdapter(categoryList, this);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategories.setAdapter(categoryAdapter);

        // Cài đặt RecyclerView cho sản phẩm
        productAdapter = new MenuProductListAdapter(productList);
        recyclerProducts.setLayoutManager(new GridLayoutManager(this, 2)); // Hiển thị 2 cột
        recyclerProducts.setAdapter(productAdapter);
    }

    private void setupListeners() {
        // Sự kiện cho nút quay lại
        imageViewBack.setOnClickListener(v -> finish());

        // Sự kiện cho nút voucher
        layoutVoucher.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, VoucherManagementActivity.class);
            startActivity(intent);
        });

        // Sự kiện cho menu 3 chấm
        imageViewAction3.setOnClickListener(this::showOptionsMenu);
    }

    /**
     * Tải danh sách các danh mục từ API
     */
    private void loadCategories() {
        String token = "Bearer " + sessionManager.fetchAuthToken();
        if (token.equals("Bearer null")) {
            Toast.makeText(this, "Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getCategories(token).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();

                    // Tự động tải sản phẩm của danh mục đầu tiên nếu có
                    if (!categoryList.isEmpty()) {
                        onCategoryClick(categoryList.get(0), 0);
                        // Cập nhật lại adapter để item đầu tiên được highlight
                        categoryAdapter.notifyItemChanged(0);
                    }
                } else {
                    Toast.makeText(MenuActivity.this, "Tải danh mục thất bại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Toast.makeText(MenuActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Tải danh sách sản phẩm dựa trên categoryId được chọn
     *
     * @param categoryId ID của danh mục
     */
    private void loadProductsByCategory(String categoryId) {
        String token = "Bearer " + sessionManager.fetchAuthToken();
        if (token.equals("Bearer null")) return;

        // Truyền categoryId vào API, nếu là null, backend sẽ hiểu là lấy tất cả
        apiService.getProducts(token, categoryId).enqueue(new Callback<List<ProductItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductItem>> call, @NonNull Response<List<ProductItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MenuActivity.this, "Tải sản phẩm thất bại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductItem>> call, @NonNull Throwable t) {
                Toast.makeText(MenuActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Được gọi khi người dùng nhấn vào một item danh mục trong CategoryAdapter
     *
     * @param category Đối tượng Category đã được nhấn
     * @param position Vị trí của item trong danh sách
     */
    @Override
    public void onCategoryClick(Category category, int position) {
        // Tải lại danh sách sản phẩm tương ứng với danh mục vừa được nhấn
        loadProductsByCategory(category.getId());
    }

    /**
     * Hiển thị menu tùy chọn khi nhấn vào icon 3 chấm
     *
     * @param view View neo của menu
     */
    private void showOptionsMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_three_point, popup.getMenu()); // Đảm bảo tên file menu là đúng

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_shift_history) {
                startActivity(new Intent(MenuActivity.this, ShiftHistoryActivity.class));
                return true;
            } else if (itemId == R.id.menu_open_shift) {
                Toast.makeText(this, "Mở ca", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_close_shift) {
                Toast.makeText(this, "Đóng ca", Toast.LENGTH_SHORT).show();
                return true;
            }
            // ... Thêm các case khác cho các item menu ...
            return false;
        });
        popup.show();
    }
}