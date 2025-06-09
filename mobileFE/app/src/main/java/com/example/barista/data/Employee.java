package com.example.barista.data;

public class Employee {
    private String id;
    private String fullName;
    private String role; // Ví dụ: "Quản lý", "Nhân viên"
    private String email;

    public Employee(String fullName, String role, String email) {
        this.fullName = fullName;
        this.role = role;
        this.email = email;
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
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
