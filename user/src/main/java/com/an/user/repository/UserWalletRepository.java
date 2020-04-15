package com.an.user.repository;

import com.an.user.entity.UserWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserWalletRepository extends JpaRepository<UserWalletEntity, Long> {

    List<UserWalletEntity> findByUserId(Long userId);

    UserWalletEntity findByUserIdAndType(Long userId, String type);
}
