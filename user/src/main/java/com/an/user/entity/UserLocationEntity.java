package com.an.user.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.GeoIndexed;

@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationEntity {
    @Id
    private Long userId;
    @GeoIndexed
    private Point location;
    private String token;
    @TimeToLive
    private Long expiration;
}
