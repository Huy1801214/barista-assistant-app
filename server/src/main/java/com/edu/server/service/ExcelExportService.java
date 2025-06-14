package com.edu.server.service;

import com.edu.server.dto.WorkShiftHistoryDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportShiftHistoryToExcel(List<WorkShiftHistoryDto> historyList) throws IOException {
        String[] columns = {"Tên Nhân Viên", "Ngày", "Giờ Dự Kiến", "Giờ Thực Tế", "Tổng Giờ Làm", "Trạng Thái"};

        // Tạo một Workbook (file Excel) mới
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Tạo một Sheet (trang tính)
            Sheet sheet = workbook.createSheet("LichSuChamCong");

            // Tạo Font cho Header
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            // Tạo Cell Style cho Header
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Tạo hàng Header
            Row headerRow = sheet.createRow(0);

            // Viết các cột vào hàng Header
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }

            // Điền dữ liệu vào các hàng
            int rowIdx = 1;
            for (WorkShiftHistoryDto history : historyList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(history.getEmployeeName());
                row.createCell(1).setCellValue(history.getShiftDate().toString());
                row.createCell(2).setCellValue(history.getFormattedScheduledTime());
                row.createCell(3).setCellValue(history.getFormattedActualTime());
                row.createCell(4).setCellValue(history.getTotalWorkedHours());
                row.createCell(5).setCellValue(getDisplayStatus(history.getAttendanceStatus()));
            }

            // Tự động điều chỉnh độ rộng các cột
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ghi workbook vào ByteArrayOutputStream
            workbook.write(out);

            // Trả về một InputStream từ dữ liệu đã ghi
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // Phương thức phụ để chuyển đổi trạng thái sang tiếng Việt
    private String getDisplayStatus(String status) {
        switch (status) {
            case "ON_TIME":
                return "Đúng giờ";
            case "LATE_ARRIVAL":
                return "Đi trễ";
            case "EARLY_DEPARTURE":
                return "Về sớm";
            case "BOTH":
                return "Trễ & Sớm";
            default:
                return "Không xác định";
        }
    }
}