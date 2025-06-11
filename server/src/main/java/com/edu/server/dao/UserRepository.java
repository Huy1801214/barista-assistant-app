package com.edu.server.dao;

import com.edu.server.collection.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// @Repository: Đánh dấu đây là một Spring Data Repository
@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    /**
     * Tìm kiếm người dùng dựa trên email duy nhất.
     * Rất quan trọng cho chức năng đăng nhập.
     * Sử dụng Optional để xử lý trường hợp không tìm thấy người dùng một cách an toàn.
     * @param email Email cần tìm
     * @return Một Optional chứa User nếu tìm thấy, ngược lại là Optional rỗng.
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Tìm tất cả người dùng thuộc về một cửa hàng cụ thể.
     * Hữu ích cho việc quản lý nhân viên của cửa hàng.
     * @param storeId ID của cửa hàng
     * @return Danh sách các User thuộc cửa hàng đó.
     */
    List<UserEntity> findByStoreId(String storeId);
    List<UserEntity> findByStoreIdAndRoleAndIsActive(String storeId, UserEntity.Role role, boolean isActive);
}
