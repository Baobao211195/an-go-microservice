package com.an.booking.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Set;

@RedisHash("driverOffEntity")
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DriverOffEntity {
    @Id
    private Long id;
    private Set<Long> lstDriver;
}
