package com.an.user.entity;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("userTruckLocationEntity")
public class UserTruckLocationEntity extends UserLocationEntity{
}
