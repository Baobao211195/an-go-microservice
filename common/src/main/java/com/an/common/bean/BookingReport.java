package com.an.common.bean;

import lombok.*;

@Data
@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookingReport {
    private Long driverId;
    private Long serviceId;
    private Long bookingNum;
    private Long moneyTotal;
}
