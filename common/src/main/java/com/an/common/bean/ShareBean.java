package com.an.common.bean;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShareBean {
    private List<List<DriverSchedule>> mapDriverSchedule;
    private List<DriverSchedule> lstDriverSchedule;
    private List<DriverSchedulePath> lstDriverSchedulePath;
}
