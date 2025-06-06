package com.example.barista.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.barista.R;

public class EmployeeDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập layout cho Activity này
        setContentView(R.layout.employee_dashboard);

        // Bạn có thể thêm code để xử lý các view trong dashboard ở đây

        CardView cardTaiQuay = findViewById(R.id.cardTaiQuay); // id của CardView trong item_function_taiquay.xml
        cardTaiQuay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashboardActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
