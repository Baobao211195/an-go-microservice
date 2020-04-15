package com.an.booking.service;

import com.an.common.bean.BookingRating;

import java.util.List;

public interface BookingRatingService {

    public BookingRating findByBookingId (Long bookingId);

    public List<BookingRating> findByDriverId(Long driverId);

    public BookingRating createBookingRating (Long bookingId, Long driverId, Long custId, String ratingNotes, Double rating);
}
