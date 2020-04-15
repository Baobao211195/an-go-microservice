package com.an.user.repository;

import com.an.user.entity.UserCar7LocationEntity;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserCar7LocationRepository extends CrudRepository<UserCar7LocationEntity, Long> {

    List<UserCar7LocationEntity> findByLocationNear(Point point, Distance distance);

    List<UserCar7LocationEntity> findByLocationWithin(Circle circle);

    List<UserCar7LocationEntity> findTop10ByLocationNear(Point point, Distance distance);
}
