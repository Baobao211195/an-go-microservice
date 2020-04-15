package com.an.catalog.repository;

import com.an.catalog.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query(value = "SELECT SYSDATE()", nativeQuery = true)
    Date getSysdate();

    List<NotificationEntity> findByTypeAndStatusAndPushDatetime(String type, String status, Date pushDatetime);

    List<NotificationEntity> findAllByStatusAndCreateDatetimeGreaterThan(String status, Date createDatetime);
}
