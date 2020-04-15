package com.an.booking.repository;

import com.an.booking.entity.DriverCancelEntity;
import org.springframework.data.repository.CrudRepository;

public interface DriverCancelRepository extends CrudRepository<DriverCancelEntity, Long> {
    DriverCancelEntity findByBookingId(Long bookingId);
}
