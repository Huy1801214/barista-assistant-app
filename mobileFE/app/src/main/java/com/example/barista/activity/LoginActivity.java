
package com.example.barista.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barista.R;

import com.example.barista.request.LoginRequest;
import com.example.barista.request.LoginResponse;
import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // Khai báo các thành phần UI
    private TextInputEditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgotPassword, textViewDeviceLogin, textViewRegister;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // Đảm bảo tên file layout là login.xml

        // 1. Khởi tạo các thành phần logic
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);

        // 2. Kiểm tra xem người dùng đã đăng nhập trước đó chưa
        // Nếu đã có token, chuyển thẳng đến màn hình chính mà không cần đăng nhập lại.
        if (sessionManager.fetchAuthToken() != null) {
            navigateToDashboard();
            return; // Dừng việc thực thi các code còn lại trong onCreate
        }

        // 3. Ánh xạ các View từ layout XML
        initViews();

        // 4. Thiết lập các sự kiện lắng nghe (click)
        setupListeners();
    }

    /**
     * Phương thức tập trung việc ánh xạ View
     */
    private void initViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewDeviceLogin = findViewById(R.id.textViewDeviceLogin);
        textViewRegister = findViewById(R.id.textViewRegister);
    }

    /**
     * Phương thức tập trung việc cài đặt các sự kiện OnClickListener
     */
    private void setupListeners() {
        // Sự kiện cho nút Đăng nhập
        buttonLogin.setOnClickListener(v -> {
            String email = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Kiểm tra đầu vào
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, getString(R.string.input_username_password_required), Toast.LENGTH_SHORT).show();
            } else {
                // Gọi phương thức xử lý logic đăng nhập
                performLogin(email, password);
            }
        });

        // Sự kiện cho "Đăng ký ngay"
        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Các sự kiện khác (hiện tại chỉ hiển thị Toast)
        textViewForgotPassword.setOnClickListener(v -> Toast.makeText(LoginActivity.this, getString(R.string.forgot_password_toast), Toast.LENGTH_SHORT).show());
        textViewDeviceLogin.setOnClickListener(v -> Toast.makeText(LoginActivity.this, getString(R.string.device_login_toast), Toast.LENGTH_SHORT).show());
    }

    /**
     * Thực hiện cuộc gọi API để đăng nhập
     *
     * @param email    Email người dùng nhập
     * @param password Mật khẩu người dùng nhập
     */
    private void performLogin(String email, String password) {
        // Vô hiệu hóa nút đăng nhập để tránh người dùng click nhiều lần trong khi đang xử lý
        buttonLogin.setEnabled(false);
        // (Tùy chọn) Có thể hiển thị một ProgressBar ở đây

        // Tạo đối tượng Request Body để gửi lên server
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Thực hiện cuộc gọi mạng bất đồng bộ bằng Retrofit
        apiService.loginUser(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                // Kích hoạt lại nút đăng nhập dù thành công hay thất bại
                buttonLogin.setEnabled(true);

                // Kiểm tra xem cuộc gọi có thành công và có body trả về không
                if (response.isSuccessful() && response.body() != null) {
                    // Đăng nhập thành công, server trả về mã 2xx
                    String token = response.body().getAccessToken();
                    sessionManager.saveAuthToken(token); // Lưu token vào SharedPreferences

                    Toast.makeText(LoginActivity.this, getString(R.string.login_successful_toast), Toast.LENGTH_SHORT).show();
                    navigateToDashboard();
                } else {
                    // Đăng nhập thất bại (sai email/password, tài khoản bị khóa...)
                    // Server trả về mã lỗi như 401, 403...
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại! Vui lòng kiểm tra lại thông tin.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Kích hoạt lại nút đăng nhập
                buttonLogin.setEnabled(true);
                // Lỗi này xảy ra khi không có kết nối mạng, server không phản hồi, hoặc lỗi parsing...
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Điều hướng đến màn hình chính sau khi đăng nhập thành công
     */
    private void navigateToDashboard() {
        Intent intent = new Intent(LoginActivity.this, EmployeeDashboardActivity.class);
        // Cờ này sẽ xóa tất cả các Activity trước đó khỏi stack,
        // để người dùng không thể nhấn nút Back quay lại màn hình Login.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // Kết thúc LoginActivity hiện tại
        finish();
    }
}