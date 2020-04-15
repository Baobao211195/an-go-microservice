package com.an.catalog.repository;

import com.an.catalog.entity.AppConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppConfigRepository extends JpaRepository<AppConfigEntity, Long> {
    List<AppConfigEntity> findByConfigCodeAndStatus(String configCode, String status);
}
