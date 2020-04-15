package com.an.common.bean;

import lombok.*;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserLocation {
    private Long userId;
    private Double x;
    private Double y;
    private Long expiration;
    private String token;
}
