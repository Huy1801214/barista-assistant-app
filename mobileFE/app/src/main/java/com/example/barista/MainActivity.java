package com.example.barista;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.barista.activity.ComfirmOrder;
import com.example.barista.data.Cart;
import com.example.barista.data.ProductItem;
import com.example.barista.data.ProductItems;
import com.example.barista.module.Sharedable;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button orderButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        testOrderMenu();
        orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(v -> testMenu());
    }

    private void testOrderMenu() {
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
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);

    }
}