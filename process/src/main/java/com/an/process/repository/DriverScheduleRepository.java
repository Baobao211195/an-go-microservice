package com.an.process.repository;

import com.an.process.entity.DriverScheduleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface DriverScheduleRepository extends CrudRepository<DriverScheduleEntity, Long> {

    @Query(value = "SELECT SYSDATE()", nativeQuery = true)
    Date getSysdate();

    @Query(nativeQuery = true, value = "select * from driver_schedule where schedule_date = date(sysdate()) and from_hour = hour(sysdate()) and from_minute = minute(sysdate()) and status = '1'")
    List<DriverScheduleEntity> findByScheduleDateAndStatus();
}
