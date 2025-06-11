package com.edu.server.api;

import com.edu.server.collection.UserEntity;
import com.edu.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

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
}