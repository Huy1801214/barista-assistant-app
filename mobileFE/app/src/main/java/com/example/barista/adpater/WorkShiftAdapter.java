package com.example.barista.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private final String userRole; // Lưu vai trò người dùng để hiển thị UI phù hợp

    public WorkShiftAdapter(Context context, List<WorkShift> shiftList, OnShiftActionListener listener, String userRole) {
        this.context = context;
        this.shiftList = shiftList;
        this.listener = listener;
        this.userRole = userRole;
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
        TextView textViewEmployeeName, textViewScheduledTime, textViewActualTime, textViewStatus, textViewShiftName;
        MaterialButton buttonClockIn, buttonClockOut;
        ImageView buttonMoreOptions;

        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            statusIndicator = itemView.findViewById(R.id.status_indicator);
            textViewEmployeeName = itemView.findViewById(R.id.textViewEmployeeName);
            textViewScheduledTime = itemView.findViewById(R.id.textViewScheduledTime);
            textViewActualTime = itemView.findViewById(R.id.textViewActualTime);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            buttonClockIn = itemView.findViewById(R.id.buttonClockIn);
            buttonClockOut = itemView.findViewById(R.id.buttonClockOut);
            textViewShiftName = itemView.findViewById(R.id.textViewShiftName);
            buttonMoreOptions = itemView.findViewById(R.id.buttonMoreOptions);
        }

        void bind(final WorkShift shift, final int position) {
            if (shift.getAssignedEmployeeName() != null) {
                textViewEmployeeName.setText(shift.getAssignedEmployeeName());
            } else {
                textViewEmployeeName.setText("Không rõ nhân viên");
            }
            textViewScheduledTime.setText("Dự kiến: " + shift.getFormattedScheduledTime());
            textViewActualTime.setText("Thực tế: " + shift.getFormattedActualTime());

            if (shift.getShiftName() != null && !shift.getShiftName().isEmpty()) {
                textViewShiftName.setText(shift.getShiftName());
                textViewShiftName.setVisibility(View.VISIBLE);
            } else {
                textViewShiftName.setVisibility(View.GONE);
            }

            buttonClockIn.setVisibility(View.GONE);
            buttonClockOut.setVisibility(View.GONE);
            textViewStatus.setVisibility(View.GONE);
            buttonMoreOptions.setVisibility(View.GONE);

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

            if ("OWNER".equals(userRole) || "MANAGER".equals(userRole)) {
                buttonMoreOptions.setVisibility(View.VISIBLE);
            }

            buttonClockIn.setOnClickListener(v -> listener.onClockIn(shift.getId(), position));
            buttonClockOut.setOnClickListener(v -> listener.onClockOut(shift.getId(), position));
            buttonMoreOptions.setOnClickListener(v -> listener.onMoreOptionsClicked(shift, v));
        }
    }
}