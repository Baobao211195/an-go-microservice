package com.an.common.bean;

import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DriverBusy {
    private Long id;
    private Set<Long> lstDriver;
}
