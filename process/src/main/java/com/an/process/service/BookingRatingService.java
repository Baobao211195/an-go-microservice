package com.an.process.service;

import com.an.common.bean.BookingRating;

import java.util.List;

public interface BookingRatingService {
    List<BookingRating> findByDriverIdAndRatingNotNull(Long driverId);

    List<BookingRating> findAvgRating();
}
