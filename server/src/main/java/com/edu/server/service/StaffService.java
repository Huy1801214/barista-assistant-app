package com.edu.server.service;

import com.edu.server.collection.UserEntity;
import com.edu.server.dao.UserRepository;
import com.edu.server.dto.StaffRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Lấy storeId của người quản lý đang đăng nhập
    private String getCurrentUserStoreId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getStoreId();
    }

    // Lấy danh sách nhân viên
    public List<UserEntity> getStaffForCurrentStore() {
        String storeId = getCurrentUserStoreId();
        // Lấy tất cả user của cửa hàng, trừ OWNER
        return userRepository.findByStoreIdAndRoleNot(storeId, UserEntity.Role.OWNER);
    }

    // Tạo nhân viên mới
    public UserEntity createStaff(StaffRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã được sử dụng.");
        }

        UserEntity newStaff = new UserEntity();
        newStaff.setFullName(request.getFullName());
        newStaff.setEmail(request.getEmail());
        newStaff.setPassword(passwordEncoder.encode(request.getPassword()));
        newStaff.setPhoneNumber(request.getPhoneNumber());
        newStaff.setRole(request.getRole());
        newStaff.setStoreId(getCurrentUserStoreId()); // Gán vào cùng cửa hàng với quản lý
        newStaff.setActive(true);

        return userRepository.save(newStaff);
    }

    // Cập nhật thông tin nhân viên
    public UserEntity updateStaff(String staffId, StaffRequestDto request) {
        UserEntity staff = userRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        // TODO: Kiểm tra xem quản lý có quyền sửa nhân viên này không (cùng storeId)

        staff.setFullName(request.getFullName());
        staff.setPhoneNumber(request.getPhoneNumber());
        staff.setRole(request.getRole());

        // Chỉ cập nhật mật khẩu nếu có mật khẩu mới được cung cấp
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            staff.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userRepository.save(staff);
    }

    // Xóa (vô hiệu hóa) nhân viên
    public void deactivateStaff(String staffId) {
        UserEntity staff = userRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        staff.setActive(false); // Thay vì xóa, chúng ta vô hiệu hóa tài khoản
        userRepository.save(staff);
    }
}
