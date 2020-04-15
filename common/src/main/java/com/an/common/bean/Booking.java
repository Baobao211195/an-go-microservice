package com.an.common.bean;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Booking {
    private Long bookingId;
    private Long custId;
    private Long driverId;
    private String bookingFrom;
    private String bookingTo;
    private Long fee;
    private Long discount;
    private Long tax;
    private Long totalFee;
    private String paymentType;
    private Long userWalletId;
    private Long userPromotionId;
    private Long serviceId;
    private String status;
    private Date startDatetime;
    private Date endDatetime;
    private Date bookingDatetime;
    private String bookingNotes;
    private Double fromX;
    private Double fromY;
    private Double toX;
    private Double toY;
    private String custToken;
    private String province;
    private Long driverScheduleId;
    private Double distance;
    private String custMobile;
    private String custName;
    private int retry;
    private Long bookingNum;
    private Long moneyTotal;
    private Long remainDays;
    private Long streetFee;
    private String paymentStatus;
    private List<BookingDetail> lstBookingDetail;
    private List<DriverSchedule> lstDriverSchedule;
    private List<User> lstUser;
}
