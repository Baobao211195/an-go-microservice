package com.an.common.bean;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    private Long userId;
    private String fullname;
    private String mobile;
    private String email;
    private String password;
    private Date createDatetime;
    private Date updateDatetime;
    private Double x;
    private Double y;
    private String status;
    private String avatar;
    private String type;
    private Double rating;
    private String socialId;
    private List<UserWallet> lstUserWallet;
    private List<UserService> lstUserService;
    private String idNoFront;
    private String idNoBack;
    private String fcmToken;
    private String province;
    private String inviteCode;
    private List<DriverSchedule> lstDriverSchedule;
    private List<Long> lstServiceGroupId;
}
