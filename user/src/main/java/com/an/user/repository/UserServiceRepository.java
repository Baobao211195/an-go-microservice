package com.an.user.repository;

import com.an.user.entity.UserServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserServiceRepository extends JpaRepository<UserServiceEntity, Long> {

    List<UserServiceEntity> findByUserIdAndStatus(Long userId, String status);

    UserServiceEntity findByUserIdAndServiceIdAndStatus(Long userId, Long serviceId, String status);
}
