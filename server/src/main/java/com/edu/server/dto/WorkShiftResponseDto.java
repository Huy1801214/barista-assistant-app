package com.edu.server.dto;

import com.edu.server.collection.WorkShiftEntity;
import java.time.LocalDateTime;

// DTO này được thiết kế để trả về cho client
public class WorkShiftResponseDto {

    private String id;
    private String storeId;
    private String assignedEmployeeId;
    private String assignedEmployeeName;
    private String shiftName;

    private LocalDateTime scheduledStartTime;
    private LocalDateTime scheduledEndTime;
    private LocalDateTime actualClockInTime;
    private LocalDateTime actualClockOutTime;
    private WorkShiftEntity.ShiftStatus status;
    private String notes;

    // Getters and Setters
    // (Bắt buộc phải có để Jackson serialize thành JSON)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
    public String getAssignedEmployeeId() { return assignedEmployeeId; }
    public void setAssignedEmployeeId(String assignedEmployeeId) { this.assignedEmployeeId = assignedEmployeeId; }
    public String getAssignedEmployeeName() { return assignedEmployeeName; }
    public void setAssignedEmployeeName(String assignedEmployeeName) { this.assignedEmployeeName = assignedEmployeeName; }
    public LocalDateTime getScheduledStartTime() { return scheduledStartTime; }
    public void setScheduledStartTime(LocalDateTime scheduledStartTime) { this.scheduledStartTime = scheduledStartTime; }
    public LocalDateTime getScheduledEndTime() { return scheduledEndTime; }
    public void setScheduledEndTime(LocalDateTime scheduledEndTime) { this.scheduledEndTime = scheduledEndTime; }
    public LocalDateTime getActualClockInTime() { return actualClockInTime; }
    public void setActualClockInTime(LocalDateTime actualClockInTime) { this.actualClockInTime = actualClockInTime; }
    public LocalDateTime getActualClockOutTime() { return actualClockOutTime; }
    public void setActualClockOutTime(LocalDateTime actualClockOutTime) { this.actualClockOutTime = actualClockOutTime; }
    public WorkShiftEntity.ShiftStatus getStatus() { return status; }
    public void setStatus(WorkShiftEntity.ShiftStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getShiftName() { return shiftName; }
    public void setShiftName(String shiftName) { this.shiftName = shiftName; }
}