package com.example.barista.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.example.barista.R;
import com.example.barista.data.Category;
import java.util.List;

public class MenuCategoryAdapter extends RecyclerView.Adapter<MenuCategoryAdapter.ViewHolder> {

    public interface OnCategorySetupListener {
        void onCategoryClick(Category category);
        void onEditClick(Category category);
        void onDeleteClick(Category category, int position);
    }

    private final List<Category> categoryList;
    private final OnCategorySetupListener listener;

    public MenuCategoryAdapter(List<Category> categoryList, OnCategorySetupListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setup_category, parent, false);
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
        TextView textViewCategoryName;
        ImageView buttonMoreOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            buttonMoreOptions = itemView.findViewById(R.id.buttonMoreOptions);
        }

        void bind(final Category category, final int position) {
            textViewCategoryName.setText(category.getName());
            itemView.setOnClickListener(v -> listener.onCategoryClick(category));
            buttonMoreOptions.setOnClickListener(v -> showOptionsMenu(v, category, position));
        }

        private void showOptionsMenu(View view, Category category, int position) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            popup.getMenuInflater().inflate(R.menu.menu_edit_delete, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.action_edit) {
                    listener.onEditClick(category);
                    return true;
                } else if (itemId == R.id.action_delete) {
                    listener.onDeleteClick(category, position);
                    return true;
                }
                return false;
            });
            popup.show();
        }
    }
}