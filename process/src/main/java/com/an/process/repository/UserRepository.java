package com.an.process.repository;

import com.an.process.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "SELECT SYSDATE()", nativeQuery = true)
    Date getSysdate();

    UserEntity findByInviteCode(String inviteCode);
}
