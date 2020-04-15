package com.an.booking.repository;

import com.an.booking.entity.BookingEntity;
import com.an.common.bean.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query(value = "SELECT SYSDATE()", nativeQuery = true)
    Date getSysdate();

    @Query("select new BookingEntity (a.driverId, a.serviceId, count(a.driverId), sum(a.totalFee)) from BookingEntity a where a.driverId = ?1 and a.status = '2' and a.endDatetime >= date(sysdate()) group by a.driverId, a.serviceId")
    List<BookingEntity> getBookingReportByDay(Long driverId);

    @Query("select new BookingEntity (a.driverId, a.serviceId, count(a.driverId), sum(a.totalFee)) from BookingEntity a where a.driverScheduleId = ?1 and a.status not in ('3', '4', '5') and a.endDatetime >= date(sysdate()) group by a.driverId, a.serviceId")
    List<BookingEntity> getBookingDetailByDriverScheduleId(Long driverScheduleId);

    List<BookingEntity> findByDriverIdAndStatus(Long driverId, String status);

    List<BookingEntity> findByCustIdAndStatus(Long custId, String status);

    List<BookingEntity> findByDriverScheduleId(Long driverScheduleId);

    List<BookingEntity> findByDriverIdAndServiceIdAndStatusAndStartDatetimeGreaterThanEqual(Long driverId, Long serviceId, String status, Date startDatetime);

    List<BookingEntity> findByCustIdAndServiceIdAndStatusAndStartDatetimeGreaterThanEqual(Long custId, Long serviceId, String status, Date startDatetime);

    @Query(nativeQuery = true, value = "select * from booking a where a.service_id in (6,7,8) and a.cust_id = ?1 and a.start_datetime >= date(sysdate()) order by a.start_datetime")
    List<BookingEntity> findShareBookingByCustId(Long custId);

    @Query("select a from BookingEntity a where a.custId = ?1 and a.status in (0,1,2,3) order by a.bookingDatetime desc")
    List<BookingEntity> findBookingHistoryByCustId(Long custId);

    @Query("select a from BookingEntity a where a.driverId = ?1 and a.status in (0,1,2,3) order by a.bookingDatetime desc")
    List<BookingEntity> findBookingHistoryByDriverId(Long driverId);
}
