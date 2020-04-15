package com.an.catalog.repository;

import com.an.catalog.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {

    @Query(value = "SELECT SYSDATE()", nativeQuery = true)
    Date getSysdate();

    List<PromotionEntity> findByServiceIdAndStatus(Long serviceId, String status);

    @Query(value = "select new PromotionEntity(a.promotionId, a.name, a.content, a.type, a.value, a.maxValue, a.startDatetime, a.endDatetime, a.status, a.serviceId, b.userPromotionId) from PromotionEntity a, UserPromotionEntity b where a.promotionId = b.promotionId and a.serviceId = ?1 and a.status = '1' and b.userId = ?2 and b.status = '1' and b.times > 0")
    List<PromotionEntity> findByUserIdAndServiceId(Long serviceId, Long userId);
}
