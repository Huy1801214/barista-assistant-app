package com.edu.server.service;

import com.edu.server.collection.UserEntity;
import com.edu.server.collection.WorkShiftEntity;
import com.edu.server.dao.UserRepository;
import com.edu.server.dao.WorkShiftRepository;
import com.edu.server.dto.WorkShiftRequest;
import com.edu.server.dto.WorkShiftResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WorkShiftService {

    @Autowired
    private WorkShiftRepository workShiftRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Lấy thông tin UserEntity của người dùng đang đăng nhập từ SecurityContext.
     *
     * @return UserEntity của người dùng hiện tại.
     */
    private UserEntity getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database"));
    }

    /**
     * Phương thức private để chuyển đổi một WorkShiftEntity sang WorkShiftResponseDto.
     * Phương thức này sẽ "làm giàu" dữ liệu bằng cách lấy tên của nhân viên.
     *
     * @param entity Đối tượng WorkShiftEntity từ database.
     * @return Một WorkShiftResponseDto đã có đầy đủ thông tin để hiển thị.
     */
    private WorkShiftResponseDto convertToDto(WorkShiftEntity entity) {
        WorkShiftResponseDto dto = new WorkShiftResponseDto();

        dto.setId(entity.getId());
        dto.setStoreId(entity.getStoreId());
        dto.setAssignedEmployeeId(entity.getAssignedEmployeeId());
        dto.setScheduledStartTime(entity.getScheduledStartTime());
        dto.setScheduledEndTime(entity.getScheduledEndTime());
        dto.setActualClockInTime(entity.getActualClockInTime());
        dto.setActualClockOutTime(entity.getActualClockOutTime());
        dto.setShiftName(determineShiftName(entity.getScheduledStartTime().toLocalTime()));
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());

        // Lấy tên nhân viên từ ID và gán vào DTO
        if (entity.getAssignedEmployeeId() != null) {
            userRepository.findById(entity.getAssignedEmployeeId()).ifPresent(user -> {
                dto.setAssignedEmployeeName(user.getFullName());
            });
        }

        // Gán giá trị mặc định nếu không tìm thấy tên nhân viên
        if (dto.getAssignedEmployeeName() == null) {
            dto.setAssignedEmployeeName("Không xác định");
        }

        return dto;
    }

    private String determineShiftName(LocalTime startTime) {
        // Định nghĩa các khung giờ
        final LocalTime MORNING_SHIFT_START = LocalTime.of(7, 0);
        final LocalTime AFTERNOON_SHIFT_START = LocalTime.of(12, 0);
        final LocalTime EVENING_SHIFT_START = LocalTime.of(17, 0);
        final LocalTime NIGHT_SHIFT_END = LocalTime.of(22, 0);

        // So sánh giờ bắt đầu
        // `isBefore(exclusive)` và `!isAfter(inclusive)`
        if (!startTime.isBefore(MORNING_SHIFT_START) && startTime.isBefore(AFTERNOON_SHIFT_START)) {
            return "Ca 1";
        } else if (!startTime.isBefore(AFTERNOON_SHIFT_START) && startTime.isBefore(EVENING_SHIFT_START)) {
            return "Ca 2";
        } else if (!startTime.isBefore(EVENING_SHIFT_START) && !startTime.isAfter(NIGHT_SHIFT_END)) {
            return "Ca 3";
        } else {
            return "Ca 4";
        }
    }


    /**
     * Quản lý tạo một ca làm việc mới cho nhân viên.
     *
     * @param request Dữ liệu đầu vào từ client.
     * @return WorkShiftResponseDto của ca làm việc vừa tạo.
     */
    public WorkShiftResponseDto createShift(WorkShiftRequest request) {
        UserEntity manager = getCurrentUser();

        WorkShiftEntity shift = new WorkShiftEntity();
        shift.setStoreId(manager.getStoreId());
        shift.setAssignedEmployeeId(request.getAssignedEmployeeId());
        shift.setScheduledStartTime(request.getScheduledStartTime());
        shift.setScheduledEndTime(request.getScheduledEndTime());
        shift.setNotes(request.getNotes());
        shift.setStatus(WorkShiftEntity.ShiftStatus.SCHEDULED);

        WorkShiftEntity savedShift = workShiftRepository.save(shift);
        return convertToDto(savedShift);
    }

    /**
     * Lấy tất cả các ca làm việc của cửa hàng trong một khoảng thời gian (dành cho quản lý).
     *
     * @param start Thời gian bắt đầu tìm kiếm.
     * @param end   Thời gian kết thúc tìm kiếm.
     * @return Danh sách các WorkShiftResponseDto.
     */
    public List<WorkShiftResponseDto> getShiftsForStore(LocalDateTime start, LocalDateTime end) {
        String storeId = getCurrentUser().getStoreId();
        List<WorkShiftEntity> shifts = workShiftRepository.findByStoreIdAndScheduledStartTimeBetween(storeId, start, end);
        return shifts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Lấy tất cả các ca làm việc của chính nhân viên đang đăng nhập.
     *
     * @param start Thời gian bắt đầu tìm kiếm.
     * @param end   Thời gian kết thúc tìm kiếm.
     * @return Danh sách các WorkShiftResponseDto.
     */
    public List<WorkShiftResponseDto> getMyShifts(LocalDateTime start, LocalDateTime end) {
        String employeeId = getCurrentUser().getId();
        List<WorkShiftEntity> shifts = workShiftRepository.findByAssignedEmployeeIdAndScheduledStartTimeBetween(employeeId, start, end);
        return shifts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Nhân viên thực hiện chấm công vào ca.
     *
     * @param shiftId ID của ca làm việc cần chấm công.
     * @return WorkShiftResponseDto của ca làm việc sau khi đã cập nhật.
     */
    public WorkShiftResponseDto clockIn(String shiftId) {
        UserEntity employee = getCurrentUser();
        WorkShiftEntity shift = workShiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found with id: " + shiftId));

        if (!Objects.equals(shift.getAssignedEmployeeId(), employee.getId())) {
            throw new SecurityException("You can only clock in for your own shift.");
        }

        if (shift.getStatus() != WorkShiftEntity.ShiftStatus.SCHEDULED) {
            throw new IllegalStateException("Cannot clock in. Shift is not in SCHEDULED state.");
        }

        shift.setActualClockInTime(LocalDateTime.now());
        shift.setStatus(WorkShiftEntity.ShiftStatus.IN_PROGRESS);
        WorkShiftEntity updatedShift = workShiftRepository.save(shift);
        return convertToDto(updatedShift);
    }

    /**
     * Nhân viên thực hiện chấm công ra ca.
     *
     * @param shiftId ID của ca làm việc cần chấm công.
     * @return WorkShiftResponseDto của ca làm việc sau khi đã cập nhật.
     */
    public WorkShiftResponseDto clockOut(String shiftId) {
        UserEntity employee = getCurrentUser();
        WorkShiftEntity shift = workShiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found with id: " + shiftId));

        if (!Objects.equals(shift.getAssignedEmployeeId(), employee.getId())) {
            throw new SecurityException("You can only clock out for your own shift.");
        }

        if (shift.getStatus() != WorkShiftEntity.ShiftStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot clock out. Shift is not in IN_PROGRESS state.");
        }

        shift.setActualClockOutTime(LocalDateTime.now());
        shift.setStatus(WorkShiftEntity.ShiftStatus.COMPLETED);
        WorkShiftEntity updatedShift = workShiftRepository.save(shift);
        return convertToDto(updatedShift);
    }

    /**
     * Quản lý thực hiện hủy một ca làm việc đã lên lịch.
     *
     * @param shiftId ID của ca làm việc cần hủy.
     */
    public void cancelShift(String shiftId) {
        UserEntity manager = getCurrentUser();
        WorkShiftEntity shift = workShiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found with id: " + shiftId));

        if (!Objects.equals(shift.getStoreId(), manager.getStoreId())) {
            throw new SecurityException("You can only cancel shifts in your own store.");
        }

        shift.setStatus(WorkShiftEntity.ShiftStatus.CANCELED);
        workShiftRepository.save(shift);
    }
}