package com.an.common.bean;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserInvite {
    private Long userInviteId;
    private Long userId;
    private String inviteCode;
    private String status;
    private Date insertDatetime;
}
