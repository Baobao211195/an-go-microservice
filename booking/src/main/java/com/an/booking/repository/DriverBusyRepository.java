package com.an.booking.repository;

import com.an.booking.entity.DriverBusyEntity;
import org.springframework.data.repository.CrudRepository;

public interface DriverBusyRepository extends CrudRepository<DriverBusyEntity, Long> {
}
