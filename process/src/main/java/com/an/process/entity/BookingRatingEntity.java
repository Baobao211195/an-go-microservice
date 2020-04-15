package com.an.process.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "booking_rating", schema = "an_service", catalog = "")
public class BookingRatingEntity {
    private Long bookingRatingId;
    private Long driverId;
    private Long custId;
    private Long bookingId;
    private Date ratingDatetime;
    private String ratingNotes;
    private Double rating;
    private Double avgRating;

    public BookingRatingEntity(){}

    public BookingRatingEntity(Double avgRating, Long driverId){
        this.avgRating = avgRating;
        this.driverId = driverId;
    }

    @Id
    @Column(name = "booking_rating_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getBookingRatingId() {
        return bookingRatingId;
    }

    public void setBookingRatingId(Long bookingRatingId) {
        this.bookingRatingId = bookingRatingId;
    }

    @Basic
    @Column(name = "driver_id")
    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    @Basic
    @Column(name = "cust_id")
    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
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
    @Column(name = "rating_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRatingDatetime() {
        return ratingDatetime;
    }

    public void setRatingDatetime(Date ratingDatetime) {
        this.ratingDatetime = ratingDatetime;
    }

    @Basic
    @Column(name = "rating_notes")
    public String getRatingNotes() {
        return ratingNotes;
    }

    public void setRatingNotes(String ratingNotes) {
        this.ratingNotes = ratingNotes;
    }

    @Basic
    @Column(name = "rating")
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Transient
    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingRatingEntity that = (BookingRatingEntity) o;
        return Objects.equals(bookingRatingId, that.bookingRatingId) &&
                Objects.equals(driverId, that.driverId) &&
                Objects.equals(custId, that.custId) &&
                Objects.equals(bookingId, that.bookingId) &&
                Objects.equals(ratingDatetime, that.ratingDatetime) &&
                Objects.equals(ratingNotes, that.ratingNotes) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingRatingId, driverId, custId, bookingId, ratingDatetime, ratingNotes, rating);
    }
}
