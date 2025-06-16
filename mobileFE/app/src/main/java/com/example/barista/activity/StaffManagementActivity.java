package com.example.barista.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.barista.R;
import com.example.barista.adpater.EmployeeAdapter;

import com.example.barista.data.Employee;
import com.example.barista.service.ApiClient;
import com.example.barista.service.ApiService;
import com.example.barista.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffManagementActivity extends AppCompatActivity implements EmployeeAdapter.OnStaffSetupListener {

    // Views
    private MaterialToolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddEmployee;
    private ProgressBar progressBar;

    // Logic
    private EmployeeAdapter adapter;
    private List<Employee> employeeList = new ArrayList<>();
    private ApiService apiService;
    private SessionManager sessionManager;

    // Launcher để nhận kết quả từ màn hình Add/Edit
    private final ActivityResultLauncher<Intent> addEditStaffLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Nếu có kết quả OK (thêm/sửa thành công), tải lại danh sách
                    loadStaffFromServer();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_management);

        initViews();
        initLogic();
        setupToolbar();
        setupRecyclerView();
        setupListeners();

        loadStaffFromServer(); // Tải dữ liệu thật thay vì dữ liệu giả
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerViewStaff);
        fabAddEmployee = findViewById(R.id.fabAddEmployee);
        // Giả sử bạn đã thêm ProgressBar vào layout với id là "progressBar"
        progressBar = findViewById(R.id.progressBar);
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

    private void setupRecyclerView() {
        adapter = new EmployeeAdapter(this, employeeList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setupListeners() {
        fabAddEmployee.setOnClickListener(v -> {
            // Mở màn hình thêm mới
            Intent intent = new Intent(this, AddEditStaffActivity.class);
            addEditStaffLauncher.launch(intent);
        });
    }

    private void loadStaffFromServer() {
        setLoading(true);
        String token = "Bearer " + sessionManager.fetchAuthToken();

        apiService.getAllStaff(token).enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(@NonNull Call<List<Employee>> call, @NonNull Response<List<Employee>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    employeeList.clear();
                    employeeList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(StaffManagementActivity.this, "Tải danh sách nhân viên thất bại.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Employee>> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(StaffManagementActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deactivateStaff(Employee employee, int position) {
        setLoading(true);
        String token = "Bearer " + sessionManager.fetchAuthToken();

        apiService.deleteStaff(token, employee.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    // Tải lại toàn bộ danh sách để cập nhật trạng thái
                    loadStaffFromServer();
                    Toast.makeText(StaffManagementActivity.this, "Đã vô hiệu hóa: " + employee.getFullName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StaffManagementActivity.this, "Thao tác thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(StaffManagementActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Thực thi các phương thức của OnStaffSetupListener ---
    @Override
    public void onEditClick(Employee employee) {
        // Mở màn hình sửa, truyền dữ liệu của nhân viên
        Intent intent = new Intent(this, AddEditStaffActivity.class);
        intent.putExtra("EMPLOYEE_ID", employee.getId());
        // Truyền các dữ liệu khác để điền sẵn vào form
        intent.putExtra("EMPLOYEE_FULL_NAME", employee.getFullName());
        intent.putExtra("EMPLOYEE_EMAIL", employee.getEmail());
        intent.putExtra("EMPLOYEE_PHONE", employee.getPhoneNumber());
        intent.putExtra("EMPLOYEE_ROLE", employee.getRole());

        addEditStaffLauncher.launch(intent);
    }

    @Override
    public void onDeleteClick(Employee employee, int position) {
        // Hiển thị dialog xác nhận trước khi vô hiệu hóa
        new AlertDialog.Builder(this)
                .setTitle("Vô hiệu hóa tài khoản")
                .setMessage("Bạn có chắc chắn muốn vô hiệu hóa nhân viên '" + employee.getFullName() + "'?")
                .setPositiveButton("Vô hiệu hóa", (dialog, which) -> deactivateStaff(employee, position))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void setLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }
}