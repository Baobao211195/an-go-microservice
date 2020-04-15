package com.an.booking.service;

import com.an.common.bean.DriverSchedulePath;

import java.util.List;

public interface DriverSchedulePathService {

    List<DriverSchedulePath> findByDriverSchedulePathGroupIdAndStatus(String driverSchedulePathGroupId, String status);

    DriverSchedulePath insertDriverSchedulePath(String driverSchedulePathGroupId, Double x, Double y, Long orderNumber);

    List<DriverSchedulePath> insertListDriverSchedulePath(String driverSchedulePathGroupId, List<DriverSchedulePath> lstDriverSchedulePath);

    DriverSchedulePath updateDriverSchedulePath(Long driverSchedulePathId, Double x, Double y, String status);
}
