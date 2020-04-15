package com.an.booking.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.Set;

@RedisHash("driverBusyEntity")
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DriverBusyEntity {
    @Id
    private Long id;
    private Set<Long> lstDriver;
}
