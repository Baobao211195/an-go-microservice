package com.an.user.entity;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("userCarLocationEntity")
public class UserCarLocationEntity extends UserLocationEntity{
}
