package com.edu.server.dao;

import com.edu.server.collection.WorkShiftEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkShiftRepository extends MongoRepository<WorkShiftEntity, String> {

    // Tìm tất cả ca làm việc của một cửa hàng trong một khoảng thời gian
    List<WorkShiftEntity> findByStoreIdAndScheduledStartTimeBetween(
            String storeId, LocalDateTime start, LocalDateTime end);

    // Tìm tất cả ca làm việc của một nhân viên cụ thể trong một khoảng thời gian
    List<WorkShiftEntity> findByAssignedEmployeeIdAndScheduledStartTimeBetween(
            String employeeId, LocalDateTime start, LocalDateTime end);
}