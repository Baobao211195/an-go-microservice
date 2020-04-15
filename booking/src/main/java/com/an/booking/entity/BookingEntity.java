package com.an.booking.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "booking", schema = "an_service", catalog = "")
public class BookingEntity {
    private Long bookingId;
    private Long custId;
    private Long driverId;
    private String bookingFrom;
    private String bookingTo;
    private Long fee;
    private Long discount;
    private Long tax;
    private Long totalFee;
    private String paymentType;
    private Long userWalletId;
    private Long userPromotionId;
    private Long serviceId;
    private String status;
    private Date startDatetime;
    private Date endDatetime;
    private Date bookingDatetime;
    private String bookingNotes;
    private Double fromX;
    private Double fromY;
    private Double toX;
    private Double toY;
    private String custToken;
    private Long bookingNum;
    private Long moneyTotal;
    private Long driverScheduleId;
    private Double distance;
    private Long streetFee;
    private String paymentStatus;

    public BookingEntity(){

    }

    public BookingEntity(Long driverId, Long serviceId, Long bookingNum, Long moneyTotal){
        this.driverId = driverId;
        this.serviceId = serviceId;
        this.bookingNum = bookingNum;
        this.moneyTotal = moneyTotal;
    }

    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
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
    @Column(name = "driver_id")
    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    @Basic
    @Column(name = "booking_from")
    public String getBookingFrom() {
        return bookingFrom;
    }

    public void setBookingFrom(String bookingFrom) {
        this.bookingFrom = bookingFrom;
    }

    @Basic
    @Column(name = "booking_to")
    public String getBookingTo() {
        return bookingTo;
    }

    public void setBookingTo(String bookingTo) {
        this.bookingTo = bookingTo;
    }

    @Basic
    @Column(name = "fee")
    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    @Basic
    @Column(name = "discount")
    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    @Basic
    @Column(name = "tax")
    public Long getTax() {
        return tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

    @Basic
    @Column(name = "total_fee")
    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    @Basic
    @Column(name = "payment_type")
    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @Basic
    @Column(name = "user_wallet_id")
    public Long getUserWalletId() {
        return userWalletId;
    }

    public void setUserWalletId(Long userWalletId) {
        this.userWalletId = userWalletId;
    }

    @Basic
    @Column(name = "user_promotion_id")
    public Long getUserPromotionId() {
        return userPromotionId;
    }

    public void setUserPromotionId(Long userPromotionId) {
        this.userPromotionId = userPromotionId;
    }

    @Basic
    @Column(name = "service_id")
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "start_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    @Basic
    @Column(name = "end_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    @Basic
    @Column(name = "booking_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getBookingDatetime() {
        return bookingDatetime;
    }

    public void setBookingDatetime(Date bookingDatetime) {
        this.bookingDatetime = bookingDatetime;
    }

    @Basic
    @Column(name = "booking_notes")
    public String getBookingNotes() {
        return bookingNotes;
    }

    public void setBookingNotes(String bookingNotes) {
        this.bookingNotes = bookingNotes;
    }

    @Basic
    @Column(name = "from_x")
    public Double getFromX() {
        return fromX;
    }

    public void setFromX(Double fromX) {
        this.fromX = fromX;
    }

    @Basic
    @Column(name = "from_y")
    public Double getFromY() {
        return fromY;
    }

    public void setFromY(Double fromY) {
        this.fromY = fromY;
    }

    @Basic
    @Column(name = "to_x")
    public Double getToX() {
        return toX;
    }

    public void setToX(Double toX) {
        this.toX = toX;
    }

    @Basic
    @Column(name = "to_y")
    public Double getToY() {
        return toY;
    }

    public void setToY(Double toY) {
        this.toY = toY;
    }

    @Basic
    @Column(name = "cust_token")
    public String getCustToken() {
        return custToken;
    }

    public void setCustToken(String custToken) {
        this.custToken = custToken;
    }

    @Basic
    @Column(name = "driver_schedule_id")
    public Long getDriverScheduleId() {
        return driverScheduleId;
    }

    public void setDriverScheduleId(Long driverScheduleId) {
        this.driverScheduleId = driverScheduleId;
    }

    @Basic
    @Column(name = "distance")
    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Basic
    @Column(name = "street_fee")
    public Long getStreetFee() {
        return streetFee;
    }

    public void setStreetFee(Long streetFee) {
        this.streetFee = streetFee;
    }

    @Basic
    @Column(name = "payment_status")
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Transient
    public Long getBookingNum() {
        return bookingNum;
    }

    public void setBookingNum(Long bookingNum) {
        this.bookingNum = bookingNum;
    }

    @Transient
    public Long getMoneyTotal() {
        return moneyTotal;
    }

    public void setMoneyTotal(Long moneyTotal) {
        this.moneyTotal = moneyTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity that = (BookingEntity) o;
        return Objects.equals(bookingId, that.bookingId) &&
                Objects.equals(custId, that.custId) &&
                Objects.equals(driverId, that.driverId) &&
                Objects.equals(bookingFrom, that.bookingFrom) &&
                Objects.equals(bookingTo, that.bookingTo) &&
                Objects.equals(fee, that.fee) &&
                Objects.equals(discount, that.discount) &&
                Objects.equals(tax, that.tax) &&
                Objects.equals(totalFee, that.totalFee) &&
                Objects.equals(paymentType, that.paymentType) &&
                Objects.equals(userWalletId, that.userWalletId) &&
                Objects.equals(userPromotionId, that.userPromotionId) &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(startDatetime, that.startDatetime) &&
                Objects.equals(endDatetime, that.endDatetime) &&
                Objects.equals(bookingDatetime, that.bookingDatetime) &&
                Objects.equals(bookingNotes, that.bookingNotes) &&
                Objects.equals(fromX, that.fromX) &&
                Objects.equals(fromY, that.fromY) &&
                Objects.equals(toX, that.toX) &&
                Objects.equals(toY, that.toY) &&
                Objects.equals(custToken, that.custToken) &&
                Objects.equals(driverScheduleId, that.driverScheduleId) &&
                Objects.equals(distance, that.distance) &&
                Objects.equals(streetFee, that.streetFee) &&
                Objects.equals(paymentStatus, that.paymentStatus)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, custId, driverId, bookingFrom, bookingTo, fee, discount, tax, totalFee, paymentType, userWalletId, userPromotionId, serviceId, status, startDatetime, endDatetime, bookingDatetime, bookingNotes, fromX, fromY, toX, toX, custToken, driverScheduleId, distance, streetFee, paymentStatus);
    }
}
