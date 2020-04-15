package com.an.user.repository;

import com.an.user.entity.UserCarLocationEntity;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserCarLocationRepository extends CrudRepository<UserCarLocationEntity, Long> {

    List<UserCarLocationEntity> findByLocationNear(Point point, Distance distance);

    List<UserCarLocationEntity> findByLocationWithin(Circle circle);

    List<UserCarLocationEntity> findTop10ByLocationNear(Point point, Distance distance);
}
