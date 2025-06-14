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

public class MenuProductListAdapter extends RecyclerView.Adapter<MenuProductListAdapter.ViewHolder>{
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewProductName;
        TextView textViewProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewProduct = itemView.findViewById(R.id.productList_imageViewProduct);
            textViewProductName = itemView.findViewById(R.id.productList_textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.productList_textViewProductPrice);
        }
    }

    private List<ProductItem> itemList;
    public MenuProductListAdapter(List<ProductItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_placeholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductItem item = itemList.get(position);
        holder.textViewProductName.setText(item.getName());
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.textViewProductPrice.setText(currencyFormat.format(item.getItemPrice()));
        // Load thumbnail image using Glide or Picasso
        Glide.with(holder.itemView.getContext()).load(item.getThumbnailUrl()).centerCrop().error(R.drawable.ic_launcher_background).into(holder.imageViewProduct);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
