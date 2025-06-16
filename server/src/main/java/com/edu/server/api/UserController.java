package com.edu.server.api;

import com.edu.server.collection.UserEntity;
import com.edu.server.dao.UserRepository;
import com.edu.server.service.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Endpoint để lấy danh sách nhân viên đang hoạt động.
     * Chỉ có OWNER và MANAGER mới được quyền gọi.
     * @return Danh sách các nhân viên.
     */
    @GetMapping("/staff")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<List<UserEntity>> getStaffList() {
        List<UserEntity> staffList = userService.getActiveStaffForCurrentStore();
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/info")
    public ResponseEntity<UserEntity> getUserInfo() {
        UserEntity u = userService.getCurrentUser();
        return ResponseEntity.ok(u);
    }

    // API thêm nhân viên
    @PostMapping("/api/employees")
    public ResponseEntity<UserEntity> createEmployee(@RequestBody UserEntity user) {
        UserEntity currentUser = userService.getCurrentUser();
        user.setRole(UserEntity.Role.STAFF); // Gán vai trò mặc định
        user.setStoreId(currentUser.getStoreId()); // Gán storeId từ người tạo
        user.setActive(true);
        user.setPassword(passwordEncoder.encode("123456")); // mật khẩu mặc định
        return ResponseEntity.ok(userRepository.save(user));
    }

    // API sửa nhân viên
    @PutMapping("/api/employees/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody UserEntity updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setFullName(updatedUser.getFullName());
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());
            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }

    // API xóa nhân viên (mềm) – set isActive = false
    @DeleteMapping("/api/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        return userRepository.findById(id).map(user -> {
            user.setActive(false);
            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }
}