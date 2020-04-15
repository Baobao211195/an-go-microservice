package com.an.booking.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.Set;

@RedisHash("driverCancelEntity")
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DriverCancelEntity {
    @Id
    private Long bookingId;
    private Set<Long> lstDriver;
    @TimeToLive
    private Long expiration;
}
