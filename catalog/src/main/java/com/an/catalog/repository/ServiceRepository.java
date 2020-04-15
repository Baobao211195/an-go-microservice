package com.an.catalog.repository;

import com.an.catalog.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    List<ServiceEntity> findAllByStatus(String status);

    List<ServiceEntity> findByServiceGroupIdAndStatus(Long serviceGroupId, String status);
}
