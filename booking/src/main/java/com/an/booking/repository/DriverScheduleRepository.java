package com.an.booking.repository;

import com.an.booking.entity.DriverScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DriverScheduleRepository extends JpaRepository<DriverScheduleEntity, Long> {

    @Query(value = "SELECT SYSDATE()", nativeQuery = true)
    Date getSysdate();

    List<DriverScheduleEntity> findByDriverIdAndServiceIdAndScheduleDateGreaterThan(Long driverId, Long serviceId, Date scheduleDate);

}
