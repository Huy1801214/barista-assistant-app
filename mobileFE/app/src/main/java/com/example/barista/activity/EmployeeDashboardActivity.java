package com.example.barista.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.barista.R;
import com.example.barista.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmployeeDashboardActivity extends AppCompatActivity {
    private TextView textViewUserName;
    private TextView textViewUserRole;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập layout cho Activity này
        setContentView(R.layout.employee_dashboard);

        sessionManager = new SessionManager(this);
        textViewUserName = findViewById(R.id.employeeName);
        textViewUserRole = findViewById(R.id.employeePosition);
        displayUserInfo();

        CardView cardTaiQuay = findViewById(R.id.cardTaiQuay); // id của CardView trong item_function_taiquay.xml
        cardTaiQuay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashboardActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_settings) {
                Intent intent = new Intent(EmployeeDashboardActivity.this, ThietLapActivity.class);
                startActivity(intent);
                return true;
            }
            // Handle other tabs if needed
            return false;
        });

        CardView cardBaoCao = findViewById(R.id.cardViewBaoCao);

        cardBaoCao.setOnClickListener(e -> {
            Intent intent = new Intent(EmployeeDashboardActivity.this, StatisticalActivity.class);
            startActivity(intent);
        });
    }

    private void displayUserInfo() {
        String userName = sessionManager.getUserName();
        String userRole = sessionManager.getUserRole();

        textViewUserName.setText(userName);
        textViewUserRole.setText(userRole);
    }
}
