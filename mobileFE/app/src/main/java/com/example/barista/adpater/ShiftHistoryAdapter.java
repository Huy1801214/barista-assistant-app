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
import com.example.barista.data.WorkShiftHistory;
import com.google.android.material.chip.Chip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShiftHistoryAdapter extends RecyclerView.Adapter<ShiftHistoryAdapter.HistoryViewHolder> {

    private final List<WorkShiftHistory> historyList;
    private final Context context;

    public ShiftHistoryAdapter(Context context, List<WorkShiftHistory> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shift_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        WorkShiftHistory historyItem = historyList.get(position);
        holder.bind(historyItem);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEmployeeName, textViewShiftDate, textViewScheduledTime, textViewActualTime, textViewTotalHours;
        Chip chipAttendanceStatus;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEmployeeName = itemView.findViewById(R.id.textViewEmployeeName);
            textViewShiftDate = itemView.findViewById(R.id.textViewShiftDate);
            textViewScheduledTime = itemView.findViewById(R.id.textViewScheduledTime);
            textViewActualTime = itemView.findViewById(R.id.textViewActualTime);
            textViewTotalHours = itemView.findViewById(R.id.textViewTotalHours);
            chipAttendanceStatus = itemView.findViewById(R.id.chipAttendanceStatus);
        }

        void bind(WorkShiftHistory item) {
            textViewEmployeeName.setText(item.getEmployeeName());
            textViewScheduledTime.setText("Dự kiến: " + item.getFormattedScheduledTime());
            textViewActualTime.setText("Thực tế: " + item.getFormattedActualTime());
            textViewTotalHours.setText("Tổng giờ: " + item.getTotalWorkedHours());

            // Format lại ngày tháng để hiển thị
            try {
                SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                Date date = apiFormat.parse(item.getShiftDate());
                textViewShiftDate.setText(displayFormat.format(date));
            } catch (ParseException | NullPointerException e) {
                textViewShiftDate.setText(item.getShiftDate());
                e.printStackTrace();
            }

            // Xử lý hiển thị Chip trạng thái
            switch (item.getAttendanceStatus()) {
                case "ON_TIME":
                    chipAttendanceStatus.setText("Đúng giờ");
                    chipAttendanceStatus.setChipBackgroundColorResource(R.color.status_active);
                    break;
                case "LATE_ARRIVAL":
                    chipAttendanceStatus.setText("Đi trễ");
                    chipAttendanceStatus.setChipBackgroundColorResource(R.color.status_inprogress);
                    break;
                case "EARLY_DEPARTURE":
                    chipAttendanceStatus.setText("Về sớm");
                    chipAttendanceStatus.setChipBackgroundColorResource(R.color.status_scheduled);
                    break;
                case "BOTH":
                    chipAttendanceStatus.setText("Trễ & Sớm");
                    chipAttendanceStatus.setChipBackgroundColorResource(R.color.status_expired);
                    break;
                default:
                    chipAttendanceStatus.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
