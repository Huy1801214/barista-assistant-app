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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barista.R;
import com.example.barista.data.Cart;
import com.example.barista.data.ProductItem;
import com.example.barista.data.ProductItems;
import com.example.barista.module.Sharedable;

import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void openOrderMenu() {
        testMenu();
        Intent intent = new Intent(this, ComfirmOrder.class);
        startActivity(intent);

    }

    private void testMenu() {
        Sharedable.put(Cart.ID, new Cart());
        Sharedable.put(ProductItems.ID, new ProductItems(List.of(
                new ProductItem(
                        0,
                        "Cafe",
                        10000,
                        ""
                )
        )));

        ProductItems p = (ProductItems) Sharedable.get(ProductItems.ID);

        ((Cart) Sharedable.get(Cart.ID)).addNewItem(p.getItemById(0));

    }

}
