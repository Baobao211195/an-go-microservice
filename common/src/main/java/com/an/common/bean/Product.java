package com.an.common.bean;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Product implements Serializable {
    private Long productId;
    private String productName;
    private Long productFee;
    private String productType;
    private Long discount;
    private String status;
    private Date createDatetime;
    private Date updateDatetime;
    private Long serviceId;
    private Long startMoney;
}
