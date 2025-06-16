package com.example.barista.data;

import com.google.gson.annotations.SerializedName;

public class StaffRequestDto {

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password; // Gửi null hoặc rỗng nếu không muốn thay đổi mật khẩu khi cập nhật

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("role")
    private String role; // Gửi lên dưới dạng String: "MANAGER" hoặc "STAFF"

    // Constructors
    public StaffRequestDto() {
    }

    public StaffRequestDto(String fullName, String email, String password, String phoneNumber, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getters and Setters (Rất quan trọng để Gson có thể hoạt động)
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
