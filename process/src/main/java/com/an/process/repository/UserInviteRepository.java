package com.an.process.repository;

import com.an.process.entity.UserInviteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserInviteRepository extends CrudRepository<UserInviteEntity, Long> {

    List<UserInviteEntity> findByUserId(Long userId);

    @Query(nativeQuery = true, value = "select * from user_invite a where a.status = ?1 and a.insert_datetime >= date(sysdate())")
    List<UserInviteEntity> findByStatus(String status);
}
