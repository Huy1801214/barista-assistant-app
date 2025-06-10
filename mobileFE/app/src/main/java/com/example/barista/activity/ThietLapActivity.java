package com.example.barista.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barista.R;

public class ThietLapActivity extends AppCompatActivity {

    private LinearLayout llQuanLyNhanVien;
    private LinearLayout llQuanLyCaLamViec;
    private LinearLayout llQuanLyVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thiet_lap);

        initViewsAndSetupListeners();
    }

    private void initViewsAndSetupListeners() {
        llQuanLyNhanVien = findViewById(R.id.llQuanLyNhanVien);
        llQuanLyCaLamViec = findViewById(R.id.llQuanLyCaLamViec);
        llQuanLyVoucher = findViewById(R.id.llQuanLyVoucher);

        llQuanLyNhanVien.setOnClickListener(v -> {
            Intent intent = new Intent(ThietLapActivity.this, StaffManagementActivity.class);
            startActivity(intent);
        });

        llQuanLyCaLamViec.setOnClickListener(v -> {
            Intent intent = new Intent(ThietLapActivity.this, ShiftManagementActivity.class);
            startActivity(intent);
        });

        llQuanLyVoucher.setOnClickListener(v -> {
            Intent intent = new Intent(ThietLapActivity.this, VoucherManagementActivity.class);
            startActivity(intent);
        });
    }
}