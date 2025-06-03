package com.example.barista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.barista.data.OrderItem;
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
        orderButton.setOnClickListener(v -> testOrderMenu());
    }

    private void testOrderMenu() {
        Sharedable.put("ordersItems", List.of(
                new OrderItem("Cà phê sửa đá", 10000, 1, "http://10.0.2.2:8080/storage/MeoBeo.jpg")
        ));
        Intent intent = new Intent(this, ComfirmOrder.class);
        startActivity(intent);
    }
}