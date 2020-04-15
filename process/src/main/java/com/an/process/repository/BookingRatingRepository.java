package com.an.process.repository;

import com.an.common.bean.BookingRating;
import com.an.process.entity.BookingRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRatingRepository extends JpaRepository<BookingRatingEntity, Long> {

    @Query("select new BookingRatingEntity (avg(a.rating), a.driverId) from BookingRatingEntity a group by a.driverId")
    List<BookingRatingEntity> findAvgRating();

    List<BookingRatingEntity> findByDriverIdAndRatingNotNull(Long driver);
}
