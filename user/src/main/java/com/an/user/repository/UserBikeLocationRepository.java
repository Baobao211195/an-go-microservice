package com.an.user.repository;

import com.an.user.entity.UserBikeLocationEntity;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserBikeLocationRepository extends CrudRepository<UserBikeLocationEntity, Long> {

    List<UserBikeLocationEntity> findByLocationNear(Point point, Distance distance);

    List<UserBikeLocationEntity> findByLocationWithin(Circle circle);

    List<UserBikeLocationEntity> findTop10ByLocationNear(Point point, Distance distance);
}
