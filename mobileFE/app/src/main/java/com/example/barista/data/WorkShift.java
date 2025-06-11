package com.example.barista.data;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Model này đại diện cho một Ca làm việc, ánh xạ trực tiếp với WorkShiftResponseDto từ backend.
 * Nó chứa đầy đủ thông tin để hiển thị và các phương thức tiện ích để định dạng dữ liệu đó.
 */
public class WorkShift {

    // --- Các thuộc tính nhận từ API ---

    @SerializedName("id")
    private String id;

    @SerializedName("assignedEmployeeId")
    private String assignedEmployeeId;

    @SerializedName("assignedEmployeeName")
    private String assignedEmployeeName;

    @SerializedName("scheduledStartTime")
    private String scheduledStartTime; // Nhận về dạng String ISO: "2025-06-11T08:00:00"

    @SerializedName("scheduledEndTime")
    private String scheduledEndTime;

    @SerializedName("actualClockInTime")
    private String actualClockInTime;

    @SerializedName("actualClockOutTime")
    private String actualClockOutTime;

    @SerializedName("status")
    private String status; // "SCHEDULED", "IN_PROGRESS", "COMPLETED", "CANCELED"

    @SerializedName("shiftName")
    private String shiftName;

    @SerializedName("notes")
    private String notes;


    // --- Getters ---
    // Cung cấp các phương thức để các lớp khác có thể truy cập dữ liệu.

    public String getId() {
        return id;
    }

    public String getAssignedEmployeeId() {
        return assignedEmployeeId;
    }

    public String getAssignedEmployeeName() {
        return assignedEmployeeName;
    }

    public String getScheduledStartTime() {
        return scheduledStartTime;
    }

    public String getScheduledEndTime() {
        return scheduledEndTime;
    }

    public String getActualClockInTime() {
        return actualClockInTime;
    }

    public String getActualClockOutTime() {
        return actualClockOutTime;
    }

    public String getStatus() {
        return status;
    }

    public String getShiftName() {
        return shiftName;
    }

    public String getNotes() {
        return notes;
    }


    // --- Các phương thức tiện ích để hiển thị ---
    // Các phương thức này giúp cho Adapter trở nên đơn giản hơn bằng cách
    // xử lý logic định dạng dữ liệu ngay tại model.

    /**
     * Lấy chuỗi thời gian dự kiến đã được định dạng.
     * Ví dụ: "08:00 - 16:00"
     * @return Chuỗi thời gian đã định dạng hoặc "N/A" nếu có lỗi.
     */
    public String getFormattedScheduledTime() {
        if (scheduledStartTime == null || scheduledEndTime == null) return "N/A";

        try {
            DateTimeFormatter apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalDateTime start = LocalDateTime.parse(scheduledStartTime, apiFormatter);
            LocalDateTime end = LocalDateTime.parse(scheduledEndTime, apiFormatter);
            return start.format(displayFormatter) + " - " + end.format(displayFormatter);
        } catch (DateTimeParseException e) {
            // Ghi log lỗi nếu cần và trả về giá trị mặc định
            e.printStackTrace();
            return "Lỗi giờ";
        }
    }

    /**
     * Lấy chuỗi thời gian chấm công thực tế đã được định dạng.
     * Ví dụ: "Vào: 07:55 - Ra: 16:05" hoặc "Vào: 08:01" hoặc "Chưa chấm công".
     * @return Chuỗi thời gian chấm công thực tế.
     */
    public String getFormattedActualTime() {
        if (actualClockInTime == null) return "Chưa chấm công";

        try {
            DateTimeFormatter apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String clockIn = LocalDateTime.parse(actualClockInTime, apiFormatter).format(displayFormatter);
            if (actualClockOutTime == null) {
                return "Vào: " + clockIn;
            }
            String clockOut = LocalDateTime.parse(actualClockOutTime, apiFormatter).format(displayFormatter);
            return "Vào: " + clockIn + " - Ra: " + clockOut;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return "Lỗi giờ";
        }
    }
}