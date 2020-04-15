package com.an.process.repository;

import com.an.process.entity.BookingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingRepository extends CrudRepository<BookingEntity, Long> {

    List<BookingEntity> findByDriverScheduleIdAndStatus(Long driverScheduleId, String status);
}
