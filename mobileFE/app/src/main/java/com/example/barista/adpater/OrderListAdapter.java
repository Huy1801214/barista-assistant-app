package com.example.barista.adpater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.barista.R;
import com.example.barista.data.Cart;
import com.example.barista.data.OrderItem;
import com.example.barista.data.ProductItem;
import com.example.barista.module.Sharedable;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder>  {
    private List<OrderItem> itemList;
    public OrderListAdapter(List<OrderItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem item = itemList.get(position);
        holder.productName.setText(item.getQuantity() + " x " + item.getItemName());
        holder.productPrice.setText(String.valueOf(item.getItemPrice()));
        // Load thumbnail image using Glide or Picasso
         Glide.with(holder.itemView.getContext()).load(item.getThumbnailUrl()).centerCrop().error(R.drawable.ic_launcher_background).into(holder.thumbnailImageView);
        holder.deleteFromCartButton.setOnClickListener(e -> {
            Cart cart = (Cart) Sharedable.get(Cart.ID);
            if (cart != null) {

                cart.removeItem(item.getId());
                itemList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView productName;
        TextView productPrice;
        Button deleteFromCartButton;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            deleteFromCartButton = itemView.findViewById(R.id.deleteFromCartButton);

        }

    }


}
