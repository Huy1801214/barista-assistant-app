package com.example.barista.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;
import com.example.barista.adpater.CategoryAdapter;
import com.example.barista.adpater.MenuProductListAdapter;
import com.example.barista.data.Cart;
import com.example.barista.data.Category;
import com.example.barista.data.ProductItem;
import com.example.barista.module.Sharedable;
import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity
        implements CategoryAdapter.OnCategoryClickListener, MenuProductListAdapter.OnProductClickListener {

    // Views
    private RecyclerView recyclerCategories, recyclerProducts;
    private ImageView imageViewBack, imageViewAction3;
    private LinearLayout layoutVoucher;
    private Button buttonPayment;
    private TextView textViewTotalAmountValue;

    // Logic
    private CategoryAdapter categoryAdapter;
    private MenuProductListAdapter productAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<ProductItem> productList = new ArrayList<>();

    private ApiService apiService;
    private SessionManager sessionManager;
    private Cart cart;

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

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại tổng tiền mỗi khi quay lại màn hình này (ví dụ từ màn hình order)
        updateTotalAmountValue();
    }

    private void initViews() {
        recyclerCategories = findViewById(R.id.recyclerViewCategories);
        recyclerProducts = findViewById(R.id.recyclerViewProducts);
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewAction3 = findViewById(R.id.imageViewAction3);
        layoutVoucher = findViewById(R.id.layoutVoucher);
        buttonPayment = findViewById(R.id.buttonPayment);
        textViewTotalAmountValue = findViewById(R.id.textViewTotalAmountValue);
    }

    private void initLogic() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);

        // Khởi tạo hoặc lấy giỏ hàng từ Sharedable
        if (Sharedable.get(Cart.ID) == null) {
            cart = new Cart();
            Sharedable.put(Cart.ID, cart);
        } else {
            cart = (Cart) Sharedable.get(Cart.ID);
        }
    }

    private void setupRecyclerViews() {
        categoryAdapter = new CategoryAdapter(categoryList, this);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategories.setAdapter(categoryAdapter);

        productAdapter = new MenuProductListAdapter(productList, this);
        recyclerProducts.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerProducts.setAdapter(productAdapter);
    }

    private void setupListeners() {
        imageViewBack.setOnClickListener(v -> finish());

        layoutVoucher.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, VoucherManagementActivity.class));
        });

        imageViewAction3.setOnClickListener(this::showOptionsMenu);

        buttonPayment.setOnClickListener(v -> {
            if (cart.getItems().isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn sản phẩm", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, ComfirmOrder.class));
            }
        });
    }

    private void loadCategories() {
        String token = "Bearer " + sessionManager.fetchAuthToken();
        apiService.getCategories(token).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();

                    if (!categoryList.isEmpty()) {
                        onCategoryClick(categoryList.get(0), 0);
                        categoryAdapter.notifyItemChanged(0);
                    }
                } else {
                    Toast.makeText(MenuActivity.this, "Tải danh mục thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Toast.makeText(MenuActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProductsByCategory(String categoryId) {
        String token = "Bearer " + sessionManager.fetchAuthToken();
        // Thống nhất dùng ApiService, không dùng ProductApi riêng
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

    @Override
    public void onCategoryClick(Category category, int position) {
        loadProductsByCategory(category.getId());
    }

    @Override
    public void onProductAddedToCart(ProductItem product) {
        // Logic khi nhấn vào một sản phẩm
        cart.addNewItem(product); // Mặc định thêm 1 sản phẩm
        Toast.makeText(this, "Đã thêm: " + product.getName(), Toast.LENGTH_SHORT).show();
        updateTotalAmountValue();
    }

    private void showOptionsMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_three_point, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_shift_history) {
                startActivity(new Intent(MenuActivity.this, ShiftHistoryActivity.class));
                return true;
            }
            // ... các case khác
            return false;
        });
        popup.show();
    }

    public void updateTotalAmountValue() {
        if (cart != null) {
            double totalAmount = cart.getPrice(); // Giả sử Cart có phương thức này
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            textViewTotalAmountValue.setText(currencyFormat.format(totalAmount));
        }
    }
}