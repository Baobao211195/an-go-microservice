package com.an.booking.service;

import com.an.common.bean.BookingIssue;

import java.util.List;

public interface BookingIssueService {

    public List<BookingIssue> findByBookingId (Long bookingId);

    public BookingIssue createBookingIssue (Long bookingId, String issueContent);

}
