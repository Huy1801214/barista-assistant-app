package com.example.barista.data;

import com.google.gson.annotations.SerializedName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class WorkShift {
    @SerializedName("id")
    private String id;

    @SerializedName("assignedEmployeeId")
    private String assignedEmployeeId;

    // TODO: Lý tưởng nhất, backend nên trả về cả tên nhân viên.
    // Tạm thời chúng ta sẽ hiển thị ID.
    // private String assignedEmployeeName;

    @SerializedName("scheduledStartTime")
    private String scheduledStartTime; // Nhận về dạng "yyyy-MM-dd'T'HH:mm:ss"

    @SerializedName("assignedEmployeeName")
    private String assignedEmployeeName;
    @SerializedName("scheduledEndTime")
    private String scheduledEndTime;

    @SerializedName("actualClockInTime")
    private String actualClockInTime;

    @SerializedName("actualClockOutTime")
    private String actualClockOutTime;

    @SerializedName("status")
    private String status; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELED

    // Getters
    public String getId() { return id; }
    public String getAssignedEmployeeId() { return assignedEmployeeId; }
    public String getStatus() { return status; }
    public String getAssignedEmployeeName() { return assignedEmployeeName; }


    // === PHƯƠNG THỨC TIỆN ÍCH ĐỂ HIỂN THỊ ===

    // Lấy chuỗi thời gian đã định dạng, ví dụ: "08:00 - 16:00"
    public String getFormattedScheduledTime() {
        DateTimeFormatter apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalDateTime start = LocalDateTime.parse(scheduledStartTime, apiFormatter);
            LocalDateTime end = LocalDateTime.parse(scheduledEndTime, apiFormatter);
            return start.format(displayFormatter) + " - " + end.format(displayFormatter);
        } catch (Exception e) {
            return "N/A";
        }
    }

    // Lấy chuỗi thời gian chấm công thực tế
    public String getFormattedActualTime() {
        if (actualClockInTime == null) return "Chưa chấm công";
        DateTimeFormatter apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            String clockIn = LocalDateTime.parse(actualClockInTime, apiFormatter).format(displayFormatter);
            if (actualClockOutTime == null) {
                return "Vào: " + clockIn;
            }
            String clockOut = LocalDateTime.parse(actualClockOutTime, apiFormatter).format(displayFormatter);
            return "Vào: " + clockIn + " - Ra: " + clockOut;
        } catch (Exception e) {
            return "Lỗi giờ";
        }
    }
}
