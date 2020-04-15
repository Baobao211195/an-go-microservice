package com.an.user.repository;

import com.an.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT SYSDATE()", nativeQuery = true)
    Date getSysdate();

    UserEntity getByUserId(Long userId);

    UserEntity getBySocialIdAndEmailAndType(String socialId, String email, String type);

    UserEntity getByMobileAndStatus(String mobile, String status);

    UserEntity getByMobileAndStatusIn(String mobile, List<String> lstStatus);

    UserEntity getByMobileAndTypeAndStatusIn(String mobile, String type, List<String> lstStatus);

    List<UserEntity> getByProvinceAndTypeAndStatus(String province, String type, String status);

}
