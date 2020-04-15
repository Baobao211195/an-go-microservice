package com.an.common.bean;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserService {
    private Long userServiceId;
    private Long userId;
    private Long serviceId;
    private Long productId;
    private String vehicleNo;
    private String vehicleType;
    private String status;
    private Date createDatetime;
    private Date updateDatetime;
    private Date expireDatetime;
    private String description;
    private Long serviceGroupId;
}
