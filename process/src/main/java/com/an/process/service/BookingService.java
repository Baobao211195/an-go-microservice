package com.an.process.service;

import com.an.common.bean.Booking;

import java.util.List;

public interface BookingService {

    List<Booking> findByDriverScheduleIdAndStatus(Long driverScheduleId, String status);
}
