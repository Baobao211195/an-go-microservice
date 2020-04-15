package com.an.booking.repository;

import com.an.booking.entity.BookingRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookingRatingRepository extends JpaRepository<BookingRatingEntity, Long> {
    @Query(value = "SELECT SYSDATE()", nativeQuery = true)
    Date getSysdate();

    BookingRatingEntity findByBookingId(Long bookingId);

    List<BookingRatingEntity> findByDriverId(Long driverId);
}
