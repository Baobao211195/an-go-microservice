package com.an.common.bean;

import lombok.*;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ServiceFee {
    private Long serviceFeeId;
    private Double minDistance;
    private Long minFee;
    private Long normalFee;
    private Long timeFee;
    private Long serviceId;
    private String status;
    private Double totalFee;
    private Double fee;
    private Double bonus;
}
