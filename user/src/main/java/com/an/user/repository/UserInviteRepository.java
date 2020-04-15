package com.an.user.repository;

import com.an.user.entity.UserInviteEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserInviteRepository extends CrudRepository<UserInviteEntity, Long> {

    List<UserInviteEntity> findByUserId(Long userId);
}
