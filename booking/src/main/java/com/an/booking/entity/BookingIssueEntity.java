package com.an.booking.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "booking_issue", schema = "an_service", catalog = "")
public class BookingIssueEntity {
    private Long bookingIssueId;
    private Long bookingId;
    private String issueContent;
    private Date issueDatetime;
    private String status;

    @Id
    @Column(name = "booking_issue_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getBookingIssueId() {
        return bookingIssueId;
    }

    public void setBookingIssueId(Long bookingIssueId) {
        this.bookingIssueId = bookingIssueId;
    }

    @Basic
    @Column(name = "booking_id")
    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    @Basic
    @Column(name = "issue_content")
    public String getIssueContent() {
        return issueContent;
    }

    public void setIssueContent(String issueContent) {
        this.issueContent = issueContent;
    }

    @Basic
    @Column(name = "issue_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getIssueDatetime() {
        return issueDatetime;
    }

    public void setIssueDatetime(Date issueDatetime) {
        this.issueDatetime = issueDatetime;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingIssueEntity that = (BookingIssueEntity) o;
        return Objects.equals(bookingIssueId, that.bookingIssueId) &&
                Objects.equals(bookingId, that.bookingId) &&
                Objects.equals(issueContent, that.issueContent) &&
                Objects.equals(issueDatetime, that.issueDatetime) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingIssueId, bookingId, issueContent, issueDatetime, status);
    }
}
