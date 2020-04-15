package com.an.common.bean;

import lombok.*;

import java.sql.Date;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class BookingRating {
    private Long bookingRatingId;
    private Long driverId;
    private Long custId;
    private Long bookingId;
    private Date ratingDatetime;
    private String ratingNotes;
    private Double rating;
    private Double avgRating;

    public BookingRating(Double avgRating, Long driverId) {
        this.driverId = driverId;
        this.avgRating = avgRating;
    }
}
