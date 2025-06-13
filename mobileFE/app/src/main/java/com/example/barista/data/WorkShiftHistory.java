package com.example.barista.data;

import com.google.gson.annotations.SerializedName;

public class WorkShiftHistory {

    @SerializedName("employeeId")
    private String employeeId;

    @SerializedName("employeeName")
    private String employeeName;

    @SerializedName("shiftDate")
    private String shiftDate; // "yyyy-MM-dd"

    @SerializedName("formattedScheduledTime")
    private String formattedScheduledTime; // "08:00 - 16:00"

    @SerializedName("formattedActualTime")
    private String formattedActualTime;

    @SerializedName("totalWorkedHours")
    private String totalWorkedHours; // "8h 10m"

    @SerializedName("attendanceStatus")
    private String attendanceStatus; // "ON_TIME", "LATE_ARRIVAL", etc.

    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getShiftDate() { return shiftDate; }
    public String getFormattedScheduledTime() { return formattedScheduledTime; }
    public String getFormattedActualTime() { return formattedActualTime; }
    public String getTotalWorkedHours() { return totalWorkedHours; }
    public String getAttendanceStatus() { return attendanceStatus; }
}
