package com.an.common.bean;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class FcmBean {
    private Long bookingId;
    private Long custId;
    private String custMobile;
    private Long driverId;
    private String driverMobile;
    private String driverName;
    private String bookingFrom;
    private String bookingTo;
    private String bookingNotes;
    private Double fromX;
    private Double fromY;
    private Double toX;
    private Double toY;
    private Double driverRating;
    private String vehicleType;
    private String vehicleNo;
    private Date startDatetime;
    private Long driverScheduleId;
    private Double distance;
    private int retry; // TODO added retry flow
    private Double driverX;
    private Double driverY;
    private List<DriverSchedule> lstDriverSchedule;
    private List<Long> lstAcceptedBooking;
    private List<Long> lstRejectedBooking;

    public static FcmBean createFcmBean (Booking booking){
        FcmBean fcmBean = new FcmBean();
        fcmBean.setBookingId(booking.getBookingId());
        fcmBean.setCustId(booking.getCustId());
        fcmBean.setDriverId(booking.getDriverId());
        fcmBean.setBookingFrom(booking.getBookingFrom());
        fcmBean.setBookingTo(booking.getBookingTo());
        fcmBean.setFromX(booking.getFromX());
        fcmBean.setFromY(booking.getFromY());
        fcmBean.setToX(booking.getToX());
        fcmBean.setToY(booking.getToY());
        fcmBean.setBookingNotes(booking.getBookingNotes());
        fcmBean.setStartDatetime(booking.getStartDatetime());
        fcmBean.setDriverScheduleId(booking.getDriverScheduleId());
        fcmBean.setDistance(booking.getDistance());
        fcmBean.setLstDriverSchedule(booking.getLstDriverSchedule());

        return fcmBean;
    }
}
