package com.an.common.bean;

import lombok.*;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ServiceGroup {
    private Long serviceGroupId;
    private String code;
    private String name;
    private String status;
}
