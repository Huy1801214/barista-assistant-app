package com.example.barista.request;

/**
 * Đây là đối tượng dùng để gửi dữ liệu lên server khi tạo/sửa một ca làm việc.
 * Nó chỉ chứa những trường mà client cần cung cấp.
 */
public class WorkShiftRequest {

    private String assignedEmployeeId;
    private String scheduledStartTime; // Gửi lên dạng String ISO: "2025-06-11T08:00:00"
    private String scheduledEndTime;   // Gửi lên dạng String ISO: "2025-06-11T16:00:00"
    private String notes;

    // Getters and Setters
    // (Bắt buộc phải có để thư viện Gson/Retrofit hoạt động)

    public String getAssignedEmployeeId() {
        return assignedEmployeeId;
    }

    public void setAssignedEmployeeId(String assignedEmployeeId) {
        this.assignedEmployeeId = assignedEmployeeId;
    }

    public String getScheduledStartTime() {
        return scheduledStartTime;
    }

    public void setScheduledStartTime(String scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }

    public String getScheduledEndTime() {
        return scheduledEndTime;
    }

    public void setScheduledEndTime(String scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}