package com.an.common.bean;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class DriverSchedule {
    private Long driverScheduleId;
    private Date scheduleDate;
    private String strScheduleDate;
    private Long fromHour;
    private Long fromMinute;
    private Long toHour;
    private Long toMinute;
    private Long driverId;
    private String status;
    private Date createDatetime;
    private Date updateDatetime;
    private Long serviceId;
    private String driverSchedulePathGroupId;
    private String fromPoint;
    private String toPoint;
    private Long bookingId;
    private Long numOfSchedule;
    private int numOfCust;
    private Long moneyTotal;
    private List<DriverSchedulePath> lstDriverSchedulePath;
    private List<Booking> lstBooking;
}
