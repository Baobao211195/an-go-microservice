package com.an.booking.repository;

import com.an.booking.entity.BookingCancelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BookingCancelRepository extends JpaRepository<BookingCancelEntity, Long> {
    List<BookingCancelEntity> findByUserIdAndUserTypeAndCancelDatetimeGreaterThanEqual(Long userId, String userType, Date cancelDatetime);
}
