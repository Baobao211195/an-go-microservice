package com.an.booking.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "booking_detail", schema = "an_service", catalog = "")
public class BookingDetailEntity {
    private Long bookingDetailId;
    private Long bookingId;
    private String name;
    private Long quantity;
    private Long fee;

    @Id
    @Column(name = "booking_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getBookingDetailId() {
        return bookingDetailId;
    }

    public void setBookingDetailId(Long bookingDetailId) {
        this.bookingDetailId = bookingDetailId;
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
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "quantity")
    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "fee")
    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDetailEntity that = (BookingDetailEntity) o;
        return Objects.equals(bookingDetailId, that.bookingDetailId) &&
                Objects.equals(bookingId, that.bookingId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(fee, that.fee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingDetailId, bookingId, name, quantity, fee);
    }
}
