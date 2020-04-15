package com.an.common.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SmsResponse {
    private String code;
    private String status;
}
