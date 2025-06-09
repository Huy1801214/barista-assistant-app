package com.example.barista.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barista.R;

import com.example.barista.request.RegisterRequest;
import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText etFullName, etEmail, etPassword, etPhone, etStoreName;
    Button btnRegister;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register); // Đảm bảo bạn có file layout này

        apiService = ApiClient.getApiService();

        etFullName = findViewById(R.id.editTextFullName); // ID tương ứng trong register.xml
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        etPhone = findViewById(R.id.editTextPhone);
        etStoreName = findViewById(R.id.editTextStoreName);
        btnRegister = findViewById(R.id.buttonRegister);

        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String storeName = etStoreName.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || storeName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc.", Toast.LENGTH_SHORT).show();
                return;
            }

            registerNewUser(fullName, email, password, phone, storeName);
        });
    }

    private void registerNewUser(String fullName, String email, String password, String phone, String storeName) {
        btnRegister.setEnabled(false);

        RegisterRequest registerRequest = new RegisterRequest(fullName, email, password, phone, storeName);

        apiService.registerUser(registerRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                btnRegister.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();
                    finish(); // Quay lại màn hình đăng nhập
                } else {
                    // Có thể do email đã tồn tại
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại. Email có thể đã được sử dụng.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnRegister.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}