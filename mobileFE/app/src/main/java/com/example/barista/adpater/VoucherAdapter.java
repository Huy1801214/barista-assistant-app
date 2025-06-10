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

        /**
         * Phương thức bind dữ liệu từ model Voucher vào các View
         *
         * @param voucher Đối tượng Voucher chứa dữ liệu từ API
         */
        void bind(final Voucher voucher) {
            textViewVoucherName.setText(voucher.getName());
            textViewVoucherCode.setText(voucher.getCode());

            // *** THAY ĐỔI QUAN TRỌNG 1 ***
            // Sử dụng phương thức tiện ích đã được định dạng sẵn trong model
            textViewValidUntil.setText(voucher.getFormattedValidUntil());

            // *** THAY ĐỔI QUAN TRỌNG 2 ***
            // Sử dụng phương thức getDisplayStatus() để lấy trạng thái cuối cùng
            // (đã bao gồm logic kiểm tra ngày hết hạn)
            switch (voucher.getDisplayStatus()) {
                case ACTIVE:
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.status_active));
                    break;
                case EXPIRED:
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.status_expired));
                    textViewValidUntil.setText("Đã hết hạn"); // Có thể ghi đè text nếu muốn
                    break;
                case PAUSED:
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.status_paused));
                    break;
            }

            // Các sự kiện click giữ nguyên
            imageViewMore.setOnClickListener(v -> {
                // TODO: Hiển thị PopupMenu với các tùy chọn Sửa/Xóa/Ngưng kích hoạt
                Toast.makeText(context, "Tùy chọn cho " + voucher.getName(), Toast.LENGTH_SHORT).show();
            });

            itemView.setOnClickListener(v -> {
                // TODO: Chuyển đến màn hình chi tiết voucher hoặc màn hình sửa
                Toast.makeText(context, "Xem chi tiết " + voucher.getName(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}