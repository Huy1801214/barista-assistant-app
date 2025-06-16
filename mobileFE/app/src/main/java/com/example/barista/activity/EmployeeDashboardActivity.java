package com.example.barista.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.barista.R;
import com.example.barista.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmployeeDashboardActivity extends AppCompatActivity {

    // Khai báo các Views
    private TextView textViewUserName;
    private TextView textViewUserRole;
    private SessionManager sessionManager;
    private Button buttonContact; // Thêm nút liên hệ
    private CardView cardTaiQuay;
    private CardView cardBaoCao;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_dashboard); // Đảm bảo tên layout đúng

        initLogic();
        initViews();
        setupListeners();
        displayUserInfo();
    }

    /**
     * Khởi tạo các đối tượng xử lý logic
     */
    private void initLogic() {
        sessionManager = new SessionManager(this);
    }

    /**
     * Ánh xạ các View từ layout
     */
    private void initViews() {
        textViewUserName = findViewById(R.id.employeeName);
        textViewUserRole = findViewById(R.id.employeePosition);
        buttonContact = findViewById(R.id.buttonContact); // Ánh xạ nút liên hệ
        cardTaiQuay = findViewById(R.id.cardTaiQuay); // Sửa lại ID cho đúng
        cardBaoCao = findViewById(R.id.cardViewBaoCao);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    /**
     * Cài đặt các sự kiện OnClickListener
     */
    private void setupListeners() {
        cardTaiQuay.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeDashboardActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        cardBaoCao.setOnClickListener(e -> {
            Intent intent = new Intent(EmployeeDashboardActivity.this, StatisticalActivity.class);
            startActivity(intent);
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_settings) {
                Intent intent = new Intent(EmployeeDashboardActivity.this, ThietLapActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // *** THÊM SỰ KIỆN CHO NÚT LIÊN HỆ ***
        buttonContact.setOnClickListener(v -> {
            sendEmail();
        });
    }

    /**
     * Lấy thông tin người dùng từ Session và hiển thị
     */
    private void displayUserInfo() {
        String userName = sessionManager.getUserName();
        String userRole = sessionManager.getUserRole();

        textViewUserName.setText(userName != null ? userName : "Không rõ");
        textViewUserRole.setText(userRole != null ? userRole : "Chưa có vai trò");
    }

    /**
     * Tạo và khởi chạy một Intent để gửi email qua ứng dụng email mặc định.
     */
    private void sendEmail() {
        // --- Chuẩn bị thông tin cho email ---
        String[] recipients = {"22130099@st.hcmuaf.edu.vn"};
        String subject = "[POSApp] Yêu cầu hỗ trợ từ: " + sessionManager.getUserName();
        String body = "\n\n\n--- Thông tin hệ thống (Vui lòng không xóa) ---\n" +
                "Device: " + Build.MANUFACTURER + " " + Build.MODEL + "\n" +
                "Android Version: " + Build.VERSION.RELEASE + "\n" +
                "App Version: 1.0.0\n"; // Có thể lấy từ BuildConfig

        // --- Tạo Intent ---
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // Chỉ mở các ứng dụng email
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        // --- Kiểm tra và khởi chạy ---
        try {
            startActivity(Intent.createChooser(emailIntent, "Chọn ứng dụng Email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Không tìm thấy ứng dụng email nào trên thiết bị.", Toast.LENGTH_SHORT).show();
        }
    }
}