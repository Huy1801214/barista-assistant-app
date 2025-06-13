package com.example.barista.activity;

import android.app.Activity;

import android.widget.LinearLayout;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;
import com.example.barista.adpater.MenuProductListAdapter;
import com.example.barista.data.Cart;
import com.example.barista.data.ProductItem;
import com.example.barista.data.ProductItems;
import com.example.barista.module.Sharedable;
import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.service.ProductApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {
    private Button orderButton;
    ProductApi productApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productApi = ApiClient.getClient().create(ApiService.class);
        setContentView(R.layout.menu);

        LinearLayout layoutVoucher = findViewById(R.id.layoutVoucher);
        layoutVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, VoucherManagementActivity.class);
                startActivity(intent);
            }
        });
        orderButton = findViewById(R.id.buttonPayment);
        orderButton.setOnClickListener(v -> openOrderMenu());

        ImageView threePoint = findViewById(R.id.imageViewAction3);
        threePoint.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.menu_three_point, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_open_shift) {
                    // Xử lý mở ca
                    return true;
                } else if (itemId == R.id.menu_close_shift) {
                    // Xử lý đóng ca
                    return true;
                } else if (itemId == R.id.menu_shift_history) {
                    Intent intent = new Intent(MenuActivity.this, ShiftHistoryActivity.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            });
            popup.show();
        });

        productApi.getProducts().enqueue(new Callback<List<ProductItem>>() {

            @Override
            public void onResponse(Call<List<ProductItem>> call, Response<List<ProductItem>> response) {
                assert response.body() != null;
                ProductItems productItems = new ProductItems(response.body());
                Sharedable.put(ProductItems.ID, productItems);
                var productListAdapter = new MenuProductListAdapter(productItems.getItems());
                RecyclerView productList = findViewById(R.id.recyclerViewProducts);
                productList.setAdapter(productListAdapter);

            }

            @Override
            public void onFailure(Call<List<ProductItem>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openOrderMenu() {
        Intent intent = new Intent(this, ComfirmOrder.class);
        startActivity(intent);

    }

}
