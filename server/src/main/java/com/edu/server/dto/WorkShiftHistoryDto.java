package com.edu.server.dto;

import java.time.LocalDate;

public class WorkShiftHistoryDto {
    private String employeeId;
    private String employeeName;
    private LocalDate shiftDate; // Ngày của ca làm việc
    private String formattedScheduledTime; // "08:00 - 16:00"
    private String formattedActualTime;    // "07:55 - 16:05"
    private String totalWorkedHours;       // "8h 10m"
    private String attendanceStatus;     // "ON_TIME", "LATE_ARRIVAL", "EARLY_DEPARTURE", "BOTH"

    // Getters và Setters
    // (Bắt buộc phải có để Jackson serialize)
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public LocalDate getShiftDate() { return shiftDate; }
    public void setShiftDate(LocalDate shiftDate) { this.shiftDate = shiftDate; }
    public String getFormattedScheduledTime() { return formattedScheduledTime; }
    public void setFormattedScheduledTime(String formattedScheduledTime) { this.formattedScheduledTime = formattedScheduledTime; }
    public String getFormattedActualTime() { return formattedActualTime; }
    public void setFormattedActualTime(String formattedActualTime) { this.formattedActualTime = formattedActualTime; }
    public String getTotalWorkedHours() { return totalWorkedHours; }
    public void setTotalWorkedHours(String totalWorkedHours) { this.totalWorkedHours = totalWorkedHours; }
    public String getAttendanceStatus() { return attendanceStatus; }
    public void setAttendanceStatus(String attendanceStatus) { this.attendanceStatus = attendanceStatus; }
}