package com.an.common.bean;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AppConfig implements Serializable {
    private Long appConfigId;
    private String configCode;
    private String configName;
    private String configValue;
    private String status;
}
