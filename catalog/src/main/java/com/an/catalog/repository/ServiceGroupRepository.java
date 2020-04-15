package com.an.catalog.repository;

import com.an.catalog.entity.ServiceGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceGroupRepository extends JpaRepository<ServiceGroupEntity, Long> {
}
