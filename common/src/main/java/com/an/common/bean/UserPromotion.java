package com.an.common.bean;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserPromotion {
    private Long userPromotionId;
    private Long userId;
    private Long promotionId;
    private Date startDatetime;
    private Date endDatetime;
    private String status;
    private String content;
    private Long times;

    public UserPromotion() {
    }

    public UserPromotion(Long userPromotionId, Long userId, Long promotionId, Date startDatetime, Date endDatetime, String status, String content) {
        this.userPromotionId = userPromotionId;
        this.userId = userId;
        this.promotionId = promotionId;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.status = status;
        this.content = content;
    }
}
