package com.example.barista.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barista.R;
import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThietLapActivity extends AppCompatActivity {

    // Khai báo các Views
    private LinearLayout llThietLapMenu;
    private LinearLayout llQuanLyNhanVien;
    private LinearLayout llQuanLyVoucher;
    private LinearLayout llQuanLyCaLamViec;
    private LinearLayout llLichSuChamCong;
    private LinearLayout llConnectGoogleCalendar;
    private LinearLayout llThietLapTaiKhoan; // Giả sử bạn có ID này trong XML
    private Button buttonLogout;
    private TextView textViewUserName;
    private TextView textViewStoreAddress; // Giả sử bạn có ID này trong XML

    // Khai báo các đối tượng xử lý logic
    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thiet_lap);

        // Luôn tuân theo thứ tự: Khởi tạo Logic -> Ánh xạ View -> Cài đặt Listener
        initLogic();
        initViews();
        setupListeners();

        // Hiển thị thông tin người dùng sau khi mọi thứ đã sẵn sàng
        displayUserInfo();
    }

    /**
     * Khởi tạo các đối tượng xử lý logic như ApiService và SessionManager.
     * Phải được gọi trước khi bất kỳ phương thức nào sử dụng chúng.
     */
    private void initLogic() {
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getApiService(); // *** SỬA LỖI: Khởi tạo apiService ở đây ***
    }

    /**
     * Ánh xạ các thành phần giao diện từ file layout XML.
     */
    private void initViews() {
        llThietLapMenu = findViewById(R.id.llThietLapMenu);
        llQuanLyNhanVien = findViewById(R.id.llQuanLyNhanVien);
        llQuanLyVoucher = findViewById(R.id.llQuanLyVoucher);
        llQuanLyCaLamViec = findViewById(R.id.llQuanLyCaLamViec);
        llLichSuChamCong = findViewById(R.id.llLichSuChamCong);
        llConnectGoogleCalendar = findViewById(R.id.llConnectGoogleCalendar);
        llThietLapTaiKhoan = findViewById(R.id.llThietLapTaiKhoan);
        buttonLogout = findViewById(R.id.buttonLogout);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewStoreAddress = findViewById(R.id.textViewStoreAddress);
    }

    /**
     * Cài đặt các sự kiện OnClickListener cho các nút và mục có thể nhấn.
     */
    private void setupListeners() {
        llThietLapMenu.setOnClickListener(v -> startActivity(new Intent(this, MenuSetupActivity.class)));
        llQuanLyNhanVien.setOnClickListener(v -> startActivity(new Intent(this, StaffManagementActivity.class)));
        llQuanLyVoucher.setOnClickListener(v -> startActivity(new Intent(this, VoucherManagementActivity.class)));
        llQuanLyCaLamViec.setOnClickListener(v -> startActivity(new Intent(this, ShiftManagementActivity.class)));
        llLichSuChamCong.setOnClickListener(v -> startActivity(new Intent(this, ShiftHistoryActivity.class)));
        llThietLapTaiKhoan.setOnClickListener(v -> Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show());

        buttonLogout.setOnClickListener(v -> performLogout());

        llConnectGoogleCalendar.setOnClickListener(v -> getAuthUrlAndOpenBrowser());
    }

    /**
     * Lấy thông tin người dùng từ SessionManager và hiển thị lên giao diện.
     */
    private void displayUserInfo() {
        String userName = sessionManager.getUserName();
        if (userName != null) {
            textViewUserName.setText(userName.toUpperCase());
        }
        // TODO: Lấy và hiển thị địa chỉ cửa hàng nếu có
    }

    /**
     * Gọi API để lấy URL xác thực từ backend và mở trình duyệt.
     */
    private void getAuthUrlAndOpenBrowser() {
        Toast.makeText(this, "Đang lấy link xác thực...", Toast.LENGTH_SHORT).show();

        String token = "Bearer " + sessionManager.fetchAuthToken();
        if (sessionManager.fetchAuthToken() == null) {
            Toast.makeText(this, "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            performLogout(); // Tự động đăng xuất nếu không có token
            return;
        }

        // Gọi API bằng đối tượng apiService đã được khởi tạo
        apiService.getGoogleAuthUrl(token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String authUrl = response.body();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(ThietLapActivity.this, "Không thể lấy link xác thực. Mã lỗi: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(ThietLapActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Xóa session và điều hướng người dùng về màn hình đăng nhập.
     */
    private void performLogout() {
        sessionManager.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}