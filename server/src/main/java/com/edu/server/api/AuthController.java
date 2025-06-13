package com.edu.server.api;

import com.edu.server.dto.LoginRequest;
import com.edu.server.dto.RegisterRequest;
import com.edu.server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Tiền tố chung cho các API xác thực
public class AuthController {

    @Autowired
    private AuthService authService;

    // DTO cho response đăng nhập
    public static class LoginResponse {
        private String accessToken;
        private String tokenType = "Bearer";

        public LoginResponse(String accessToken) {
            this.accessToken = accessToken;
        }

        // Getters and Setters
        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            authService.register(registerRequest);
            return ResponseEntity.ok("User registered successfully!");
        } catch (RuntimeException e) {
            // Trả về lỗi nếu có
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<com.edu.server.dto.LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        com.edu.server.dto.LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

}