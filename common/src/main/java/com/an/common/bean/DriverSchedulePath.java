package com.an.common.bean;

import lombok.*;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class DriverSchedulePath {
    private Long driverSchedulePathId;
    private String driverSchedulePathGroupId;
    private Double x;
    private Double y;
    private Long orderNumber;
    private String status;
}
