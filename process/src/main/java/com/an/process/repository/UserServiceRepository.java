package com.an.process.repository;

import com.an.process.entity.UserServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserServiceRepository extends JpaRepository<UserServiceEntity, Long> {
    List<UserServiceEntity> findByStatusAndExpireDatetimeGreaterThan(String status, Date expireDatetime);
}