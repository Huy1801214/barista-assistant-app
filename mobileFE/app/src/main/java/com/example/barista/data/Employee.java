package com.example.barista.data;

import com.google.gson.annotations.SerializedName;

public class Employee {
    @SerializedName("id")
    private String id;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("role")
    private String role; // Nhận về "MANAGER" hoặc "STAFF"
    @SerializedName("email")
    private String email;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("active")
    private boolean isActive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDisplayRole() {
        if ("MANAGER".equalsIgnoreCase(role)) return "Quản lý";
        if ("STAFF".equalsIgnoreCase(role)) return "Nhân viên";
        return "Không xác định";
    }

    // Tiện ích để lấy chữ cái đầu của tên
    public String getInitials() {
        if (fullName == null || fullName.isEmpty()) {
            return "?";
        }
        String[] parts = fullName.split(" ");
        if (parts.length > 0) {
            return parts[parts.length - 1].substring(0, 1).toUpperCase();
        }
        return fullName.substring(0, 1).toUpperCase();
    }
}
