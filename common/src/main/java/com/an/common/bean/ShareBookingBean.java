package com.an.common.bean;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ShareBookingBean {
    private Long driverId;
    private String driverName;
    private Double driverRating;
    private String driverMobile;
    private Long serviceId;
    List<Booking> lstBooking;
}
