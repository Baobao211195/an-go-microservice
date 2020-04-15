package com.an.common.bean;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserWallet {
    private Long userWalletId;
    private Long userId;
    private String type;
    private Double balance;
    private Date createDatetime;
    private Date updateDatetime;
}
