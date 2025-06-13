package com.edu.server.service;

import com.edu.server.collection.UserEntity;
import com.edu.server.dao.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    // Lấy UserEntity của người dùng đang đăng nhập
    public UserEntity getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database"));
    }

    /**
     * Lấy danh sách nhân viên (staff) đang hoạt động của cửa hàng.
     * Dành cho quản lý sử dụng khi cần gán việc.
     *
     * @return Danh sách các UserEntity.
     */
    public List<UserEntity> getActiveStaffForCurrentStore() {
        String storeId = getCurrentUser().getStoreId();
        logger.info("Đang tìm nhân viên cho storeId: {}", storeId);
        List<UserEntity> staffList = userRepository.findByStoreIdAndRoleAndIsActive(storeId, UserEntity.Role.STAFF, true);

        logger.info("Đã tìm thấy {} nhân viên.", staffList.size());

        return staffList;
    }

    // ... các phương thức service khác nếu có ...
}