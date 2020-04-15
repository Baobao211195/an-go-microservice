package com.an.booking.repository;

import com.an.booking.entity.BookingDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingDetailRepository extends JpaRepository<BookingDetailEntity, Long> {

    List<BookingDetailEntity> findByBookingId(Long bookingId);
}
