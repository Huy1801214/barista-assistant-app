package com.example.barista.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barista.R;

import com.example.barista.data.Employee;
import com.example.barista.data.StaffRequestDto;
import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditStaffActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextInputEditText etFullName, etEmail, etPassword, etPhone;
    private RadioGroup radioGroupRole;
    private RadioButton radioStaff, radioManager;
    private Button btnSave;

    private ApiService apiService;
    private SessionManager sessionManager;
    private String staffId; // Sẽ là null nếu là chế độ "Thêm mới"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_employee);

        initViews();
        initLogic();
        setupToolbar();
        checkModeAndPopulateData();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etFullName = findViewById(R.id.editTextFullName);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        etPhone = findViewById(R.id.editTextPhone);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        radioStaff = findViewById(R.id.radioStaff);
        radioManager = findViewById(R.id.radioManager);
        btnSave = findViewById(R.id.buttonSave);
    }

    private void initLogic() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void checkModeAndPopulateData() {
        staffId = getIntent().getStringExtra("EMPLOYEE_ID");
        if (staffId != null) {
            // Chế độ Sửa
            toolbar.setTitle("Sửa thông tin Nhân viên");
            etEmail.setEnabled(false); // Không cho phép sửa email
            findViewById(R.id.layoutPassword).setVisibility(View.GONE); // Ẩn ô mật khẩu

            // Điền dữ liệu cũ
            etFullName.setText(getIntent().getStringExtra("EMPLOYEE_FULL_NAME"));
            etEmail.setText(getIntent().getStringExtra("EMPLOYEE_EMAIL"));
            etPhone.setText(getIntent().getStringExtra("EMPLOYEE_PHONE"));
            String role = getIntent().getStringExtra("EMPLOYEE_ROLE");
            if ("MANAGER".equalsIgnoreCase(role)) {
                radioManager.setChecked(true);
            } else {
                radioStaff.setChecked(true);
            }
        } else {
            // Chế độ Thêm mới
            toolbar.setTitle("Thêm Nhân viên mới");
        }
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> {
            if (validateInput()) {
                saveStaff();
            }
        });
    }

    private boolean validateInput() {
        if (etFullName.getText().toString().trim().isEmpty()) {
            etFullName.setError("Tên không được để trống");
            return false;
        }
        if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.setError("Email không được để trống");
            return false;
        }
        // Chỉ yêu cầu mật khẩu khi thêm mới
        if (staffId == null && etPassword.getText().toString().isEmpty()) {
            etPassword.setError("Mật khẩu không được để trống khi tạo mới");
            return false;
        }
        return true;
    }

    private void saveStaff() {
        setLoading(true);
        String token = "Bearer " + sessionManager.fetchAuthToken();

        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String role = radioGroupRole.getCheckedRadioButtonId() == R.id.radioManager ? "MANAGER" : "STAFF";

        StaffRequestDto staffDto = new StaffRequestDto(fullName, email, password, phone, role);

        Call<Employee> apiCall;
        if (staffId != null) {
            // Sửa nhân viên
            apiCall = apiService.updateStaff(token, staffId, staffDto);
        } else {
            // Thêm mới nhân viên
            apiCall = apiService.createStaff(token, staffDto);
        }

        apiCall.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditStaffActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AddEditStaffActivity.this, "Lưu thất bại. Email có thể đã tồn tại.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(AddEditStaffActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        btnSave.setEnabled(!isLoading);
        btnSave.setText(isLoading ? "Đang lưu..." : "Lưu lại");
    }
}