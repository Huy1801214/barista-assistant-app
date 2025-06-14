package com.example.barista.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.barista.R;
import com.example.barista.data.ProductItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MenuProductListAdapter extends RecyclerView.Adapter<MenuProductListAdapter.ViewHolder> {

    /**
     * Interface để giao tiếp ngược lại với Activity khi người dùng nhấn vào một sản phẩm.
     */
    public interface OnProductClickListener {
        void onProductAddedToCart(ProductItem product);
    }

    private final List<ProductItem> itemList;
    private final OnProductClickListener listener;

    /**
     * Constructor của Adapter.
     * @param itemList Danh sách các sản phẩm để hiển thị.
     * @param listener Một đối tượng (thường là Activity) để xử lý sự kiện click.
     */
    public MenuProductListAdapter(List<ProductItem> itemList, OnProductClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_placeholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductItem item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewProductName;
        TextView textViewProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.productList_imageViewProduct);
            textViewProductName = itemView.findViewById(R.id.productList_textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.productList_textViewProductPrice);
        }

        void bind(final ProductItem item) {
            textViewProductName.setText(item.getName());

            // Định dạng giá tiền theo đơn vị tiền tệ Việt Nam (VNĐ)
            if (item.getItemPrice() != 0) {
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                textViewProductPrice.setText(currencyFormat.format(item.getItemPrice()));
            } else {
                textViewProductPrice.setText("0 đ");
            }

            // Tải ảnh thumbnail bằng Glide, hiển thị ảnh lỗi nếu không tải được
            Glide.with(itemView.getContext())
                    .load(item.getThumbnailUrl())
                    .centerCrop()
                    .into(imageViewProduct);

            // Gán sự kiện click cho toàn bộ item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductAddedToCart(item);
                }
            });
        }
    }
}