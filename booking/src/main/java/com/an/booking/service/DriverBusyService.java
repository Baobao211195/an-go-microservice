package com.an.booking.service;

import com.an.common.bean.DriverBusy;

public interface DriverBusyService {

    DriverBusy findById(Long id);

    void insertDriver(Long id, Long driverId);

    void evictDriver(Long id, Long driverId);

}
