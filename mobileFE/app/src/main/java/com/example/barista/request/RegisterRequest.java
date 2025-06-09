package com.example.barista.request;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String storeName;

    public RegisterRequest(String fullName, String email, String password, String phoneNumber, String storeName) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.storeName = storeName;
    }
}
