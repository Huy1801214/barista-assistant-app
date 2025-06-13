package com.example.barista.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barista.R;
import com.example.barista.utils.SessionManager;

public class ThietLapActivity extends AppCompatActivity {

    private LinearLayout llQuanLyNhanVien;
    private LinearLayout llQuanLyCaLamViec;
    private LinearLayout llQuanLyVoucher;
    private Button buttonLogout;
    private SessionManager sessionManager;
    private TextView textViewUserName;
    private LinearLayout llLichSuChamCong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thiet_lap);
        sessionManager = new SessionManager(this);
        initViewsAndSetupListeners();
        displayUserInfo();
    }

    private void displayUserInfo() {
        String userName = sessionManager.getUserName();
        if (userName != null) {
            textViewUserName.setText(userName.toUpperCase());
        }
    }

    private void initViewsAndSetupListeners() {
        llQuanLyNhanVien = findViewById(R.id.llQuanLyNhanVien);
        llQuanLyCaLamViec = findViewById(R.id.llQuanLyCaLamViec);
        llQuanLyVoucher = findViewById(R.id.llQuanLyVoucher);
        buttonLogout = findViewById(R.id.buttonLogout);
        textViewUserName = findViewById(R.id.textViewUserName);
        llLichSuChamCong = findViewById(R.id.llLichSuChamCong);

        llQuanLyNhanVien.setOnClickListener(v -> {
            Intent intent = new Intent(ThietLapActivity.this, StaffManagementActivity.class);
            startActivity(intent);
        });

        llQuanLyCaLamViec.setOnClickListener(v -> {
            Intent intent = new Intent(ThietLapActivity.this, ShiftManagementActivity.class);
            startActivity(intent);
        });

        llLichSuChamCong.setOnClickListener(v -> {
            Intent intent = new Intent(ThietLapActivity.this, ShiftHistoryActivity.class);
            startActivity(intent);
        });

        llQuanLyVoucher.setOnClickListener(v -> {
            Intent intent = new Intent(ThietLapActivity.this, VoucherManagementActivity.class);
            startActivity(intent);
        });
        buttonLogout.setOnClickListener(v -> {
            performLogout();
        });
    }

    private void performLogout() {
        // 1. Xóa session đã lưu (token, thông tin người dùng)
        sessionManager.clear();

        // 2. Tạo Intent để quay về màn hình Đăng nhập
        Intent intent = new Intent(ThietLapActivity.this, LoginActivity.class);

        // 3. Xóa tất cả các Activity trước đó khỏi stack
        // Điều này ngăn người dùng nhấn nút "Back" để quay lại các màn hình bên trong ứng dụng
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // 4. Bắt đầu LoginActivity
        startActivity(intent);

        // 5. Kết thúc ThietLapActivity hiện tại
        finish();
    }
}