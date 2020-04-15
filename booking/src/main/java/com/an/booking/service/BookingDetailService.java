package com.an.booking.service;

import com.an.common.bean.BookingDetail;

import java.util.List;

public interface BookingDetailService {

    public List<BookingDetail> findByBookingId (Long bookingId);

    public BookingDetail createBookingDetail (Long bookingId, String name, Long quantity, Long fee);

    public List<BookingDetail> createListBookingDetail (Long bookingId, List<BookingDetail> lstBookingDetail);
}
