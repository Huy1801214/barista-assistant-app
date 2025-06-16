package com.example.barista.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;
import com.example.barista.data.Employee;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    /**
     * Interface để giao tiếp từ Adapter ngược lại với Activity.
     */
    public interface OnStaffSetupListener {
        void onEditClick(Employee employee);

        void onDeleteClick(Employee employee, int position); // Gửi cả vị trí để dễ dàng xóa khỏi list
    }

    private final List<Employee> employeeList;
    private final Context context;
    private final OnStaffSetupListener listener;

    public EmployeeAdapter(Context context, List<Employee> employeeList, OnStaffSetupListener listener) {
        this.context = context;
        this.employeeList = employeeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.bind(employee, position);
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAvatar, textViewFullName, textViewRole;
        ImageView imageViewMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAvatar = itemView.findViewById(R.id.textViewAvatar);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            imageViewMore = itemView.findViewById(R.id.imageViewMore);
        }

        void bind(final Employee employee, final int position) {
            // Hiển thị thông tin
            textViewAvatar.setText(employee.getInitials());
            textViewFullName.setText(employee.getFullName());
            textViewRole.setText(employee.getDisplayRole());

            // Thay đổi giao diện dựa trên trạng thái active
            if (employee.isActive()) {
                // Nếu hoạt động, màu chữ bình thường
                textViewFullName.setTextColor(ContextCompat.getColor(context, R.color.text_dark_gray));
                textViewRole.setTextColor(ContextCompat.getColor(context, R.color.text_light_gray));
                textViewAvatar.getBackground().setAlpha(255); // Hoàn toàn rõ nét
            } else {
                // Nếu bị vô hiệu hóa, làm mờ đi
                textViewFullName.setTextColor(ContextCompat.getColor(context, R.color.text_light_gray));
                textViewRole.setTextColor(ContextCompat.getColor(context, R.color.text_light_gray));
                textViewAvatar.getBackground().setAlpha(128); // Mờ 50%
            }

            // Sự kiện khi nhấn vào nút 3 chấm
            imageViewMore.setOnClickListener(v -> showOptionsMenu(v, employee, position));
        }

        private void showOptionsMenu(View view, Employee employee, int position) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            popup.getMenuInflater().inflate(R.menu.menu_edit_delete, popup.getMenu());

            // Có thể thay đổi tiêu đề của menu item nếu cần
            if (!employee.isActive()) {
                // Nếu đã bị vô hiệu hóa, có thể hiển thị tùy chọn "Kích hoạt lại"
                // popup.getMenu().findItem(R.id.action_delete).setTitle("Kích hoạt lại");
            }

            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.action_edit) {
                    if (listener != null) {
                        listener.onEditClick(employee);
                    }
                    return true;
                } else if (itemId == R.id.action_delete) {
                    if (listener != null) {
                        listener.onDeleteClick(employee, position);
                    }
                    return true;
                }
                return false;
            });
            popup.show();
        }
    }
}