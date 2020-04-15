package com.an.common.bean;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Service implements Serializable {
    private Long serviceId;
    private String serviceName;
    private String status;
    private Long serviceGroupId;
}
