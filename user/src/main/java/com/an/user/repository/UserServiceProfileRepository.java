package com.an.user.repository;

import com.an.user.entity.UserServiceProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserServiceProfileRepository extends JpaRepository<UserServiceProfileEntity, Long> {

    List<UserServiceProfileEntity> getByUserServiceId(Long userServiceId);

    UserServiceProfileEntity getByUserServiceProfileId(Long userServiceProfileId);
}
