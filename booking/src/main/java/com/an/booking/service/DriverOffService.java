package com.an.booking.service;

import com.an.common.bean.DriverOff;

public interface DriverOffService {

    DriverOff findById(Long id);

    void insertDriver(Long id, Long driverId);

    void evictDriver(Long id, Long driverId);

}
