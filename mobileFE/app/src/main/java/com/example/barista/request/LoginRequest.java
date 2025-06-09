package com.example.barista.request;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    // Không cần Getters/Setters cho request, Gson sẽ tự xử lý
}
