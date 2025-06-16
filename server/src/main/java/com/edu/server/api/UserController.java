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
}