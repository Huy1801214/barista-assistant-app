package com.edu.server.api;

import com.edu.server.collection.UserEntity;
import com.edu.server.dto.StaffRequestDto;
import com.edu.server.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@PreAuthorize("hasAnyRole('OWNER', 'MANAGER')") // Chỉ quản lý/chủ mới được truy cập
public class StaffController {
    @Autowired
    private StaffService staffService;

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllStaff() {
        return ResponseEntity.ok(staffService.getStaffForCurrentStore());
    }

    @PostMapping
    public ResponseEntity<?> createStaff(@RequestBody StaffRequestDto request) {
        try {
            return new ResponseEntity<>(staffService.createStaff(request), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateStaff(@PathVariable String id, @RequestBody StaffRequestDto request) {
        return ResponseEntity.ok(staffService.updateStaff(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable String id) {
        staffService.deactivateStaff(id);
        return ResponseEntity.noContent().build();
    }
}
