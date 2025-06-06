package com.example.barista.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barista.R;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText editTextUsername, editTextPassword;
    Button buttonLogin;
    TextView textViewForgotPassword, textViewDeviceLogin, textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // Đảm bảo tên file XML đúng

        // Ánh xạ View
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewDeviceLogin = findViewById(R.id.textViewDeviceLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        // Xử lý sự kiện click cho nút Đăng nhập
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.input_username_password_required), Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý logic đăng nhập ở đây (ví dụ: gọi API, kiểm tra CSDL)
                    // Tạm thời chỉ hiển thị Toast
                    Toast.makeText(LoginActivity.this, getString(R.string.login_successful_toast) + " " + username, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, EmployeeDashboardActivity.class);

                    // 2. Bắt đầu Activity mới
                    startActivity(intent);

                    // 3. (Tùy chọn nhưng khuyến nghị) Kết thúc LoginActivity để người dùng
                    // không thể nhấn nút Back để quay lại màn hình đăng nhập
                    finish();
                }
            }
        });

        // Xử lý sự kiện click cho "Quên mật khẩu?"
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, getString(R.string.forgot_password_toast), Toast.LENGTH_SHORT).show();
                // Chuyển đến màn hình quên mật khẩu
            }
        });

        // Xử lý sự kiện click cho "Đăng nhập mã thiết bị"
        textViewDeviceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, getString(R.string.device_login_toast), Toast.LENGTH_SHORT).show();
                // Chuyển đến màn hình đăng nhập mã thiết bị
            }
        });

        // Xử lý sự kiện click cho "Đăng ký ngay"
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, getString(R.string.register_now_toast), Toast.LENGTH_SHORT).show();
                // Chuyển đến màn hình đăng ký
            }
        });
    }
}
