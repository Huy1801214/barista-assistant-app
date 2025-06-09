package com.example.barista.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;
import com.example.barista.data.Employee;


import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> employeeList;
    private Context context;

    public EmployeeAdapter(Context context, List<Employee> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.bind(employee);
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAvatar, textViewFullName, textViewRole;
        ImageView imageViewMore;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAvatar = itemView.findViewById(R.id.textViewAvatar);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            imageViewMore = itemView.findViewById(R.id.imageViewMore);
        }

        void bind(final Employee employee) {
            textViewAvatar.setText(employee.getInitials());
            textViewFullName.setText(employee.getFullName());
            textViewRole.setText(employee.getRole());

            // Xử lý sự kiện click vào nút 3 chấm
            imageViewMore.setOnClickListener(v -> {
                // Hiển thị menu tùy chọn (Sửa, Xóa...)
                // Tạm thời chỉ hiển thị Toast
                Toast.makeText(context, "Tùy chọn cho " + employee.getFullName(), Toast.LENGTH_SHORT).show();
            });

            // Xử lý sự kiện click vào cả item
            itemView.setOnClickListener(v -> {
                // Chuyển đến trang chi tiết/sửa nhân viên
                Toast.makeText(context, "Xem chi tiết " + employee.getFullName(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}