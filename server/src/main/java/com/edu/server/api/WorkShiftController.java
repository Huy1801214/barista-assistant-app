package com.edu.server.api; // Hoặc com.edu.server.controller

import com.edu.server.dto.WorkShiftRequest;
import com.edu.server.dto.WorkShiftResponseDto;
import com.edu.server.service.WorkShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/work-shifts")
public class WorkShiftController {

    @Autowired
    private WorkShiftService workShiftService;

    // Quản lý tạo ca mới
    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<WorkShiftResponseDto> createShift(@RequestBody WorkShiftRequest request) {
        WorkShiftResponseDto createdShift = workShiftService.createShift(request);
        return new ResponseEntity<>(createdShift, HttpStatus.CREATED);
    }

    // Quản lý xem tất cả ca làm việc của cửa hàng
    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<List<WorkShiftResponseDto>> getShiftsForStore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<WorkShiftResponseDto> shifts = workShiftService.getShiftsForStore(start, end);
        return ResponseEntity.ok(shifts);
    }

    // Nhân viên xem ca làm việc của mình
    @GetMapping("/my-shifts")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<WorkShiftResponseDto>> getMyShifts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<WorkShiftResponseDto> myShifts = workShiftService.getMyShifts(start, end);
        return ResponseEntity.ok(myShifts);
    }

    // Nhân viên clock-in
    @PostMapping("/{id}/clock-in")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'STAFF')")
    public ResponseEntity<WorkShiftResponseDto> clockIn(@PathVariable("id") String shiftId) {
        WorkShiftResponseDto updatedShift = workShiftService.clockIn(shiftId);
        return ResponseEntity.ok(updatedShift);
    }

    // Nhân viên clock-out
    @PostMapping("/{id}/clock-out")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'STAFF')")
    // *** SỬA LỖI 3: Thay đổi kiểu trả về từ WorkShiftEntity sang WorkShiftResponseDto ***
    public ResponseEntity<WorkShiftResponseDto> clockOut(@PathVariable("id") String shiftId) {
        WorkShiftResponseDto updatedShift = workShiftService.clockOut(shiftId);
        return ResponseEntity.ok(updatedShift);
    }

    // Quản lý hủy ca
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<Void> cancelShift(@PathVariable("id") String shiftId) {
        workShiftService.cancelShift(shiftId);
        return ResponseEntity.noContent().build();
    }
}