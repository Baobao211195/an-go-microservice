package com.an.common.bean;

import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DriverOff {
    private Long id;
    private Set<Long> lstDriver;
}
