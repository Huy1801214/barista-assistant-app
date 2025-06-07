package com.example.barista.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.barista.R;
import com.example.barista.adpater.EmployeeAdapter;
import com.example.barista.data.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeManagementActivity extends AppCompatActivity {
    private List<Employee> employeeList = new ArrayList<>();
    private EmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_management);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEmployees);
        adapter = new EmployeeAdapter(employeeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.buttonAddEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Example: Add a new employee (replace with dialog/input in real app)
                employeeList.add(new Employee("New Employee", "Staff"));
                adapter.notifyItemInserted(employeeList.size() - 1);
                Toast.makeText(EmployeeManagementActivity.this, "Employee added", Toast.LENGTH_SHORT).show();
            }
        });

        // Example data
        employeeList.add(new Employee("Alice", "Manager"));
        employeeList.add(new Employee("Bob", "Barista"));
        adapter.notifyDataSetChanged();
    }
}