package com.an.user.service;

import com.an.common.bean.UserLocation;
import com.an.user.entity.UserBikeLocationEntity;

import java.util.List;

public interface UserBikeLocationService {

    public UserBikeLocationEntity updateUserLocation (Long userId, String token, Double x, Double y);

    public List<UserLocation> findTop10UserByLocationAndDistance (Double x, Double y, Long distance);

    public UserLocation findById(Long id);

    public void evictById(Long id);
}
