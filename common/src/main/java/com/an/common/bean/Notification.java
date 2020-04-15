package com.an.common.bean;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Notification {
    private Long notificationId;
    private String name;
    private String content;
    private Date pushDatetime;
    private String status;
    private Date createDatetime;
    private Date updateDatetime;
    private String type;
}
