package com.edu.server.dto;

// DTO trả về sau khi đăng nhập thành công, chứa token
public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserInfo userInfo;

    public LoginResponse(String accessToken, UserInfo userInfo) {
        this.accessToken = accessToken;
        this.userInfo = userInfo;
    }

    // Nested class để trả về thông tin cơ bản của user
    public static class UserInfo {
        private String id;
        private String fullName;
        private String email;
        private String role;
        private String storeId;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getStoreId() { return storeId; }
        public void setStoreId(String storeId) { this.storeId = storeId; }
    }

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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}