package com.example.barista.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Model này đại diện cho một người dùng (nhân viên) khi lấy dữ liệu từ API.
 * Nó được thiết kế để chứa các thông tin cơ bản và có thể được sử dụng
 * trong nhiều ngữ cảnh khác nhau, ví dụ như hiển thị trong danh sách chọn.
 */
public class User {

    /**
     * @SerializedName: Annotation này của thư viện Gson giúp ánh xạ tên trường
     * trong JSON response từ server vào thuộc tính của class này.
     * Ví dụ: JSON có trường "id", Gson sẽ tự động gán giá trị đó vào thuộc tính 'id' này.
     */
    @SerializedName("id")
    private String id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("role")
    private String role; // Ví dụ: "OWNER", "MANAGER", "STAFF"


    // Constructors (không bắt buộc nhưng hữu ích cho việc test)
    public User() {
    }

    public User(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    @NonNull
    @Override
    public String toString() {
        return fullName != null ? fullName : "";
    }
}
