package com.an.common.bean;

import lombok.*;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ServiceFeeLarge {
    private Long serviceFeeId;
    private Long serviceId;
    private Long from;
    private Long to;
    private Double fee;
    private Double rushFee;
    private Long order;
    private String status;
    private Double totalFee;
    private boolean isRushHour;
}
