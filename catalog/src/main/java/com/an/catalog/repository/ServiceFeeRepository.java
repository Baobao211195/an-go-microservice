package com.an.catalog.repository;

import com.an.catalog.entity.ServiceFeeEntity;
import com.an.catalog.entity.ServiceFeeLargeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceFeeRepository extends JpaRepository<ServiceFeeEntity, Long> {
    List<ServiceFeeEntity> findByServiceIdAndStatus(Long serviceId, String status);
}
