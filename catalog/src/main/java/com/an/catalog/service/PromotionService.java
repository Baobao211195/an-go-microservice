package com.an.catalog.service;

import com.an.common.bean.Promotion;

import java.util.Date;
import java.util.List;

public interface PromotionService {
    public Date getSysdate();

    public Promotion getPromotionById(Long promotionId);

    public List<Promotion> findPromotionByServiceId (Long serviceId);

    public List<Promotion> findByUserIdAndServiceId(Long serviceId, Long userId);

    public Promotion createPromotion(Long serviceId, String name, String content, String type, Long value, Long maxValue, Date startDatetime, Date endDatetime);

    public Promotion updatePromotion(Promotion promotion);
}
