package com.edu.server.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// @Document: Đánh dấu class này là một entity ánh xạ tới collection "users" trong MongoDB
@Document(collection = "users")
public class UserEntity {

    // @Id: Đánh dấu trường này là khóa chính (_id trong MongoDB)
    @Id
    private String id;

    @Field("full_name")
    private String fullName;

    // @Indexed(unique = true): Tạo một chỉ mục cho trường email và đảm bảo giá trị là duy nhất
    // Rất quan trọng cho chức năng đăng nhập
    @Indexed(unique = true)
    private String email;

    // Mật khẩu phải luôn được lưu trữ ở dạng đã được mã hóa (ví dụ: bằng BCrypt)
    private String password;

    @Field("phone_number")
    private String phoneNumber;

    // Sử dụng Enum để quản lý vai trò một cách an toàn và rõ ràng
    private Role role;

    // Lưu ID của cửa hàng mà người dùng này thuộc về
    // Giúp phân tách dữ liệu giữa các cửa hàng khác nhau
    @Field("store_id")
    private String storeId;

    @Field("is_active")
    private boolean isActive = true; // Mặc định là tài khoản đang hoạt động

    // Enum cho vai trò người dùng
    public enum Role {
        OWNER,   // Chủ cửa hàng
        MANAGER, // Quản lý
        STAFF    // Nhân viên
    }

    // Constructors, Getters và Setters
    // (Bạn nên tạo chúng bằng IDE cho tiện lợi)

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
