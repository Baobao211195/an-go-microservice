package com.an.booking.service;

import com.an.common.bean.BookingCancel;

import java.util.Date;
import java.util.List;

public interface BookingCancelService {

    List<BookingCancel> findByUserIdAndUserTypeAndCancelDatetime(Long userId, String userType, Date cancelDatetime);

    BookingCancel saveBookingCancel(Long bookingId, Long userId, String userType, String reason, Date cancelDatetime);
}
