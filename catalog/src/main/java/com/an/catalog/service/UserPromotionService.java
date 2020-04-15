package com.an.catalog.service;

import com.an.common.bean.UserPromotion;

import java.util.Date;

public interface UserPromotionService {

    UserPromotion insertUserPromotionService(Long userId, Long promotionId, Date startDatetime, Date endDatetime);

    UserPromotion reduceUserPromotionTimes(Long userPromotionId);

    UserPromotion findById(Long userPromotionId);
}
