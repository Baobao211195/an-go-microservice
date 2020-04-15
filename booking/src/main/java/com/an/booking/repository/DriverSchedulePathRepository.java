package com.an.booking.repository;

import com.an.booking.entity.DriverSchedulePathEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverSchedulePathRepository extends JpaRepository<DriverSchedulePathEntity, Long> {

    List<DriverSchedulePathEntity> findByDriverSchedulePathGroupIdAndStatus(String driverSchedulePathGroupId, String status);
}
