package com.an.booking.repository;

import com.an.booking.entity.BookingIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookingIssueRepository extends JpaRepository<BookingIssueEntity, Long> {

    @Query(value = "SELECT SYSDATE()", nativeQuery = true)
    Date getSysdate();

    List<BookingIssueEntity> findByBookingId(Long bookingId);
}
