package com.an.process.service;

import com.an.common.bean.DriverSchedule;

import java.util.List;

public interface DriverScheduleService {

    List<DriverSchedule> findByScheduleDateAndStatus();

    DriverSchedule updateDriverScheduleStatus(Long driverScheduleId, String status);
}
