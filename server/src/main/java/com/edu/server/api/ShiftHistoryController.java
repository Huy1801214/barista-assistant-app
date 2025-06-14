package com.edu.server.api;

import com.edu.server.dto.WorkShiftHistoryDto;
import com.edu.server.service.ExcelExportService;
import com.edu.server.service.WorkShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/history")
public class ShiftHistoryController {

    @Autowired
    private WorkShiftService workShiftService;
    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/work-shifts")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<List<WorkShiftHistoryDto>> getShiftHistory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String employeeId) { // employeeId là tùy chọn

        List<WorkShiftHistoryDto> history = workShiftService.getShiftHistory(startDate, endDate, employeeId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/work-shifts/export")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<InputStreamResource> exportShiftHistoryToExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String employeeId) throws IOException {

        // 1. Lấy dữ liệu lịch sử giống hệt API cũ
        List<WorkShiftHistoryDto> historyList = workShiftService.getShiftHistory(startDate, endDate, employeeId);

        // 2. Gọi service để tạo file Excel trong bộ nhớ
        ByteArrayInputStream in = excelExportService.exportShiftHistoryToExcel(historyList);

        // 3. Chuẩn bị headers để trình duyệt hiểu đây là một file cần tải về
        HttpHeaders headers = new HttpHeaders();
        String filename = "LichSuChamCong_" + startDate.toLocalDate() + "_den_" + endDate.toLocalDate() + ".xlsx";
        headers.add("Content-Disposition", "attachment; filename=" + filename);

        // 4. Trả về response chứa file
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
}
