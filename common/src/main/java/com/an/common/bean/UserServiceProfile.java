package com.an.common.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@EqualsAndHashCode
public class UserServiceProfile {
    private Long userServiceProfileId;
    private Long userId;
    private String profileType;
    private String profileValue;
    private String status;
}
