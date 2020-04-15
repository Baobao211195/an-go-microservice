package com.an.catalog.repository;

import com.an.catalog.entity.UserPromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPromotionRepository extends JpaRepository<UserPromotionEntity, Long> {

    List<UserPromotionEntity> findByUserIdAndStatus(Long userId, String status);

}
