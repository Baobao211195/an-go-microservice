package com.an.common.bean;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BookingIssue {
    private Long bookingIssueId;
    private Long bookingId;
    private String issueContent;
    private Date issueDatetime;
    private String status;
}
