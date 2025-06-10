package com.edu.server.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "work_shifts")
public class WorkShiftEntity {

    @Id
    private String id;

    @Field("store_id")
    private String storeId;

    @Field("assigned_employee_id")
    private String assignedEmployeeId; // ID của nhân viên được phân công

    // Thời gian dự kiến
    @Field("scheduled_start_time")
    private LocalDateTime scheduledStartTime;

    @Field("scheduled_end_time")
    private LocalDateTime scheduledEndTime;

    // Thời gian chấm công thực tế
    @Field("actual_clock_in_time")
    private LocalDateTime actualClockInTime;

    @Field("actual_clock_out_time")
    private LocalDateTime actualClockOutTime;

    private ShiftStatus status;

    private String notes; // Ghi chú của quản lý

    public enum ShiftStatus {
        SCHEDULED,      // Đã lên lịch
        IN_PROGRESS,    // Đang diễn ra (đã clock-in)
        COMPLETED,      // Đã hoàn thành (đã clock-out)
        CANCELED        // Đã bị hủy
    }

    // Constructors, Getters và Setters
    // (Hãy tạo chúng bằng IDE của bạn)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
    public String getAssignedEmployeeId() { return assignedEmployeeId; }
    public void setAssignedEmployeeId(String assignedEmployeeId) { this.assignedEmployeeId = assignedEmployeeId; }
    public LocalDateTime getScheduledStartTime() { return scheduledStartTime; }
    public void setScheduledStartTime(LocalDateTime scheduledStartTime) { this.scheduledStartTime = scheduledStartTime; }
    public LocalDateTime getScheduledEndTime() { return scheduledEndTime; }
    public void setScheduledEndTime(LocalDateTime scheduledEndTime) { this.scheduledEndTime = scheduledEndTime; }
    public LocalDateTime getActualClockInTime() { return actualClockInTime; }
    public void setActualClockInTime(LocalDateTime actualClockInTime) { this.actualClockInTime = actualClockInTime; }
    public LocalDateTime getActualClockOutTime() { return actualClockOutTime; }
    public void setActualClockOutTime(LocalDateTime actualClockOutTime) { this.actualClockOutTime = actualClockOutTime; }
    public ShiftStatus getStatus() { return status; }
    public void setStatus(ShiftStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}