package com.example.barista.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barista.R;
import com.example.barista.data.WorkShift;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class WorkShiftAdapter extends RecyclerView.Adapter<WorkShiftAdapter.ShiftViewHolder> {

    private final List<WorkShift> shiftList;
    private final Context context;
    private final OnShiftActionListener listener;

    public WorkShiftAdapter(Context context, List<WorkShift> shiftList, OnShiftActionListener listener) {
        this.context = context;
        this.shiftList = shiftList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_work_shift, parent, false);
        return new ShiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftViewHolder holder, int position) {
        WorkShift shift = shiftList.get(position);
        holder.bind(shift, position);
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }

    class ShiftViewHolder extends RecyclerView.ViewHolder {
        View statusIndicator;
        TextView textViewEmployeeName, textViewScheduledTime, textViewActualTime, textViewStatus;
        MaterialButton buttonClockIn, buttonClockOut;

        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            statusIndicator = itemView.findViewById(R.id.status_indicator);
            textViewEmployeeName = itemView.findViewById(R.id.textViewEmployeeName);
            textViewScheduledTime = itemView.findViewById(R.id.textViewScheduledTime);
            textViewActualTime = itemView.findViewById(R.id.textViewActualTime);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            buttonClockIn = itemView.findViewById(R.id.buttonClockIn);
            buttonClockOut = itemView.findViewById(R.id.buttonClockOut);
        }

        void bind(final WorkShift shift, final int position) {
            // Hiển thị thông tin cơ bản
            // TODO: Thay thế ID bằng tên nhân viên nếu API trả về
            textViewEmployeeName.setText("Nhân viên: " + shift.getAssignedEmployeeName());
            textViewScheduledTime.setText("Dự kiến: " + shift.getFormattedScheduledTime());
            textViewActualTime.setText("Thực tế: " + shift.getFormattedActualTime());

            // Reset trạng thái của các view có điều kiện trước khi set
            buttonClockIn.setVisibility(View.GONE);
            buttonClockOut.setVisibility(View.GONE);
            textViewStatus.setVisibility(View.GONE);

            // Cập nhật UI dựa trên trạng thái của ca làm việc
            switch (shift.getStatus()) {
                case "SCHEDULED":
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.status_scheduled));
                    buttonClockIn.setVisibility(View.VISIBLE);
                    break;
                case "IN_PROGRESS":
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.status_inprogress));
                    buttonClockOut.setVisibility(View.VISIBLE);
                    break;
                case "COMPLETED":
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.status_active));
                    textViewStatus.setVisibility(View.VISIBLE);
                    textViewStatus.setText("Đã hoàn thành");
                    textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.status_active));
                    break;
                case "CANCELED":
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.status_paused));
                    textViewStatus.setVisibility(View.VISIBLE);
                    textViewStatus.setText("Đã hủy");
                    textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.status_paused));
                    break;
                default:
                    statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.text_light_gray));
                    break;
            }

            // Gán sự kiện click cho các nút, gọi đến listener trong Activity
            buttonClockIn.setOnClickListener(v -> listener.onClockIn(shift.getId(), position));
            buttonClockOut.setOnClickListener(v -> listener.onClockOut(shift.getId(), position));
        }
    }
}
