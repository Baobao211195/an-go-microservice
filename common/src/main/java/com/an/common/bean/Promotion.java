package com.an.common.bean;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Promotion implements Serializable {
    private Long promotionId;
    private String name;
    private String content;
    private String type;
    private Long value;
    private Long maxValue;
    private Date startDatetime;
    private Date endDatetime;
    private String status;
    private Long serviceId;
    private Long userPromotionId;
}
