package com.an.user.repository;

import com.an.user.entity.UserTruckLocationEntity;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserTruckLocationRepository extends CrudRepository<UserTruckLocationEntity, Long> {

    List<UserTruckLocationEntity> findByLocationNear(Point point, Distance distance);

    List<UserTruckLocationEntity> findByLocationWithin(Circle circle);

    List<UserTruckLocationEntity> findTop10ByLocationNear(Point point, Distance distance);
}
