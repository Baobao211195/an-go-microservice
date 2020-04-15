package com.an.common.bean;

import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DriverCancel {
    private Long bookingId;
    private Set<Long> lstDriver;
    private Long expiration;
}
