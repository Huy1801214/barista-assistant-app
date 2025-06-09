package com.example.barista.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;
import com.example.barista.data.Voucher;


import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private List<Voucher> voucherList;
    private Context context;

    public VoucherAdapter(Context context, List<Voucher> voucherList) {
        this.context = context;
        this.voucherList = voucherList;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = voucherList.get(position);
        holder.bind(voucher);
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    class VoucherViewHolder extends RecyclerView.ViewHolder {
        View statusIndicator;
        TextView textViewVoucherName, textViewVoucherCode, textViewValidUntil;
        ImageView imageViewMore;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            statusIndicator = itemView.findViewById(R.id.status_indicator);
            textViewVoucherName = itemView.findViewById(R.id.textViewVoucherName);
            textViewVoucherCode = itemView.findViewById(R.id.textViewVoucherCode);
            textViewValidUntil = itemView.findViewById(R.id.textViewValidUntil);
            imageViewMore = itemView.findViewById(R.id.imageViewMore);
        }

        void bind(final Voucher voucher) {
            textViewVoucherName.setText(voucher.getName());
            textViewVoucherCode.setText(voucher.getCode());
            textViewValidUntil.setText(voucher.getValidUntil());

            // Thay đổi màu sắc dựa trên trạng thái
            switch (voucher.getStatus()) {
                case ACTIVE:
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.status_active));
                    break;
                case EXPIRED:
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.status_expired));
                    break;
                case PAUSED:
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.status_paused));
                    break;
            }

            imageViewMore.setOnClickListener(v -> Toast.makeText(context, "Tùy chọn cho " + voucher.getName(), Toast.LENGTH_SHORT).show());
            itemView.setOnClickListener(v -> Toast.makeText(context, "Xem chi tiết " + voucher.getName(), Toast.LENGTH_SHORT).show());
        }
    }
}
