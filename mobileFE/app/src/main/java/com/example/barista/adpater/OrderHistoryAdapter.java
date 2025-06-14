package com.example.barista.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;
import com.example.barista.data.OrderEntity;
import com.example.barista.data.OrderItemEntity;
import com.example.barista.response.StatisticalResponse;
import com.example.barista.utils.NumberFormat;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>{
    StatisticalResponse statisticalResponse;

    public OrderHistoryAdapter(StatisticalResponse statisticalResponse) {
        this.statisticalResponse = statisticalResponse;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_order_item, parent, false);
        return new OrderHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderEntity order = statisticalResponse.getHistory().get(position);
        holder.orderId.setText(String.valueOf(order.getId()));
        holder.totalPrice.setText(NumberFormat.formatMoney(order.getTotalPrice()));
        holder.discount.setText(NumberFormat.formatMoney(order.getDiscount()));
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < order.getOrderItems().length; i++) {
            OrderItemEntity item = order.getOrderItems()[i];
            stringBuilder.append(" - ");
            stringBuilder.append(item.getItemName());
            stringBuilder.append(" x");
            stringBuilder.append(item.getQuantity());
            stringBuilder.append("\n");
        }

        holder.product.setText(stringBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return statisticalResponse.getHistory().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView totalPrice;
        TextView discount;
        TextView product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            discount = itemView.findViewById(R.id.discount);
            product = itemView.findViewById(R.id.product);
        }
    }
}
