package com.an.booking.service;

import com.an.common.bean.Booking;
import com.an.common.exception.LogicException;

import java.util.Date;
import java.util.List;

public interface BookingService {

    public Booking findById(Long bookingId);

    public List<Booking> getOngoingByDriver (Long driverId);

    public List<Booking> getOngoingByCustomer (Long custId);

    public Booking saveBooking (Booking booking);

    public Booking confirmPayment(Long bookingId, Long streetFee);

    public Booking booking(Booking booking) throws LogicException, Exception ;

    public Booking updateDriver(Long bookingId, Long driverId) throws Exception, LogicException;

    public void cancelBooking(Long bookingId, Long cancelUserId, String userType, String reason) throws LogicException, Exception ;

    public void finishBooking(Long bookingId) throws Exception, LogicException;

    public void updateOngoingBooking(Long bookingId) throws Exception, LogicException;

    public Booking updateBookingStatus(Long bookingId, String status);

    List<Booking> getBookingReportByDay(Long driverId);

    Booking getBookingDetailByDriverScheduleId(Long driverScheduleId);

    List<Booking> getUnfinishBookingByDriverIdAndServiceIdOnNowDate(Long driverId, Long serviceId);

    List<Booking> getBookingByDriverIdAndServiceIdAndStartDatetime(Long driverId, Long serviceId, Date fromDate, Date toDate);

    List<Booking> findByDriverScheduleId(Long driverScheduleId);

    void updateShareBooking (Long driverId, List<Long> lstAcceptedBookingId, List<Long> lstRefuseBookingId) throws LogicException, Exception;

    List<Booking> findShareBookingByCustId(Long custId);

    List<Booking> findBookingHistoryByCustId(Long custId);

    List<Booking> findBookingHistoryByDriverId(Long driverId);

}
