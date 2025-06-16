package com.example.barista.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.barista.R;
import com.example.barista.data.ProductItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductSetupAdapter extends RecyclerView.Adapter<ProductSetupAdapter.ViewHolder> {

    public interface OnProductSetupListener {
        void onEditClick(ProductItem product);

        void onDeleteClick(ProductItem product, int position);
    }

    private final List<ProductItem> productList;
    private final Context context;
    private final OnProductSetupListener listener;

    public ProductSetupAdapter(Context context, List<ProductItem> productList, OnProductSetupListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setup_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductItem product = productList.get(position);
        holder.bind(product, position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct, buttonMoreOptions;
        TextView textViewProductName, textViewProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            buttonMoreOptions = itemView.findViewById(R.id.buttonMoreOptions);
        }

        void bind(final ProductItem product, final int position) {
            textViewProductName.setText(product.getName());

            if (product.getItemPrice() > 0) {
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                textViewProductPrice.setText(currencyFormat.format(product.getItemPrice()));
            } else {
                textViewProductPrice.setText("Chưa có giá");
            }

            Glide.with(context)
                    .load(product.getThumbnailUrl())
                    .placeholder(R.drawable.ic_placeholder_24dp)
                    .error(R.drawable.ic_sync)
                    .into(imageViewProduct);

            buttonMoreOptions.setOnClickListener(v -> showOptionsMenu(v, product, position));

            itemView.setOnClickListener(v -> {
                // Khi nhấn vào item, cũng mở màn hình sửa
                if (listener != null) {
                    listener.onEditClick(product);
                }
            });
        }

        private void showOptionsMenu(View view, ProductItem product, int position) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            popup.getMenuInflater().inflate(R.menu.menu_edit_delete, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.action_edit) {
                    if (listener != null) {
                        listener.onEditClick(product);
                    }
                    return true;
                } else if (itemId == R.id.action_delete) {
                    if (listener != null) {
                        listener.onDeleteClick(product, position);
                    }
                    return true;
                }
                return false;
            });
            popup.show();
        }
    }
}
