package com.an.common.bean;

import lombok.*;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BookingDetail {
    private Long bookingDetailId;
    private Long bookingId;
    private String name;
    private Long quantity;
    private Long fee;
}
