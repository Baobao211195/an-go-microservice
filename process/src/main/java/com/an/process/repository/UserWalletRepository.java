package com.an.process.repository;

import com.an.process.entity.UserWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserWalletRepository extends JpaRepository<UserWalletEntity, Long> {

    List<UserWalletEntity> findByUserId(Long userId);

    UserWalletEntity findByUserIdAndType(Long userId, String type);
}
