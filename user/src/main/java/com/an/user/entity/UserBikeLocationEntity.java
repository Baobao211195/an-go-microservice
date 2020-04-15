package com.an.user.entity;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("userBikeLocationEntity")
public class UserBikeLocationEntity  extends UserLocationEntity{
}
