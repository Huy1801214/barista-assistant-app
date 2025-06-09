package com.example.barista.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;

import com.example.barista.adpater.EmployeeAdapter;
import com.example.barista.data.Employee;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class StaffManagementActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView recyclerViewStaff;
    private FloatingActionButton fabAddEmployee;
    private EmployeeAdapter adapter;
    private List<Employee> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_management);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupListeners();

        loadDummyData(); // Tạm thời dùng dữ liệu giả
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewStaff = findViewById(R.id.recyclerViewStaff);
        fabAddEmployee = findViewById(R.id.fabAddEmployee);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        employeeList = new ArrayList<>();
        adapter = new EmployeeAdapter(this, employeeList);
        recyclerViewStaff.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStaff.setAdapter(adapter);
    }

    private void setupListeners() {
        fabAddEmployee.setOnClickListener(v -> {
            // Chuyển đến màn hình Thêm/Sửa nhân viên
            Toast.makeText(this, "Mở màn hình thêm nhân viên mới", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, AddEditEmployeeActivity.class);
            // startActivity(intent);
        });
    }

    private void loadDummyData() {
        // Trong ứng dụng thật, bạn sẽ gọi API ở đây.
        // Đây là dữ liệu giả để hiển thị giao diện.
        employeeList.add(new Employee("Nguyễn Văn A", "Quản lý", "nva@example.com"));
        employeeList.add(new Employee("Trần Thị B", "Nhân viên", "ttb@example.com"));
        employeeList.add(new Employee("Lê Văn C", "Nhân viên", "lvc@example.com"));
        employeeList.add(new Employee("Phạm Thị D", "Nhân viên", "ptd@example.com"));
        employeeList.add(new Employee("Vũ Văn E", "Nhân viên", "vve@example.com"));

        adapter.notifyDataSetChanged(); // Cập nhật lại RecyclerView
    }
}