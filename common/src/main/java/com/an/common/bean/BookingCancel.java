package com.an.common.bean;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BookingCancel {
    private Long bookingCancelId;
    private Long bookingId;
    private Long userId;
    private String userType;
    private String cancelReason;
    private Date cancelDatetime;
}
