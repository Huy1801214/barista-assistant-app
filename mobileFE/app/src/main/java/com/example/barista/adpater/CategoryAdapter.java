package com.example.barista.adpater;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.barista.R;
import com.example.barista.data.Category;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int position);
    }

    private List<Category> categoryList;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0; // Vị trí của item được chọn

    public CategoryAdapter(List<Category> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_placeholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category, position);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name); // Thay bằng ID đúng trong layout của bạn
        }

        void bind(Category category, int position) {
            categoryName.setText(category.getName());

            // Thay đổi giao diện cho item được chọn
            if (selectedPosition == position) {
                itemView.setBackgroundColor(Color.parseColor("#FFF3E0")); // Màu cam nhạt
                categoryName.setTextColor(Color.parseColor("#FF6F00")); // Màu cam đậm
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT);
                categoryName.setTextColor(Color.BLACK);
            }

            itemView.setOnClickListener(v -> {
                notifyItemChanged(selectedPosition); // Cập nhật lại item cũ
                selectedPosition = getAdapterPosition();
                notifyItemChanged(selectedPosition); // Cập nhật item mới được chọn
                listener.onCategoryClick(category, selectedPosition);
            });
        }
    }
}