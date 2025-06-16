package com.edu.server.dto;

import com.edu.server.collection.UserEntity;

public class StaffRequestDto {
    private String fullName;
    private String email;
    private String password; // Bắt buộc khi tạo mới, tùy chọn khi cập nhật
    private String phoneNumber;
    private UserEntity.Role role; // MANAGER hoặc STAFF

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserEntity.Role getRole() {
        return role;
    }

    public void setRole(UserEntity.Role role) {
        this.role = role;
    }
}