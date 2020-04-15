package com.an.common.bean;

import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DailyTitle {
    private Long dailyTitleId;
    private String content;
    private Date showDatetime;
}
