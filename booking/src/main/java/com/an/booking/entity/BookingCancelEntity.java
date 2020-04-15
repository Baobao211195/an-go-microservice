package com.an.booking.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "booking_cancel", schema = "an_service", catalog = "")
public class BookingCancelEntity {
    private Long bookingCancelId;
    private Long bookingId;
    private Long userId;
    private String userType;
    private String cancelReason;
    private Date cancelDatetime;

    @Id
    @Column(name = "booking_cancel_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getBookingCancelId() {
        return bookingCancelId;
    }

    public void setBookingCancelId(Long bookingCancelId) {
        this.bookingCancelId = bookingCancelId;
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
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "user_type")
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Basic
    @Column(name = "cancel_reason")
    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    @Basic
    @Column(name = "cancel_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCancelDatetime() {
        return cancelDatetime;
    }

    public void setCancelDatetime(Date cancelDatetime) {
        this.cancelDatetime = cancelDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingCancelEntity that = (BookingCancelEntity) o;
        return Objects.equals(bookingCancelId, that.bookingCancelId) &&
                Objects.equals(bookingId, that.bookingId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(userType, that.userType) &&
                Objects.equals(cancelReason, that.cancelReason) &&
                Objects.equals(cancelDatetime, that.cancelDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingCancelId, bookingId, userId, userType, cancelReason, cancelDatetime);
    }
}
