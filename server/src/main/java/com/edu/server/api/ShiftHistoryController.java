package com.edu.server.api;

import com.edu.server.dto.WorkShiftHistoryDto;
import com.edu.server.service.WorkShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/history")
public class ShiftHistoryController {

    @Autowired
    private WorkShiftService workShiftService;

    @GetMapping("/work-shifts")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<List<WorkShiftHistoryDto>> getShiftHistory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String employeeId) { // employeeId là tùy chọn

        List<WorkShiftHistoryDto> history = workShiftService.getShiftHistory(startDate, endDate, employeeId);
        return ResponseEntity.ok(history);
    }
}
