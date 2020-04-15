package com.an.booking.service;

import com.an.common.bean.DriverCancel;

public interface DriverCancelService {
    DriverCancel getListDriverCancel(Long bookingId);

    DriverCancel updateDriverCancelForBooking(Long bookingId, Long driverId);

    void removeDriverCancel(Long bookingId);
}
