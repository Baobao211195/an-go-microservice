package com.an.catalog.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "service_fee_large", schema = "an_service", catalog = "")
public class ServiceFeeLargeEntity {
    private Long serviceFeeId;
    private Long serviceId;
    private Long from;
    private Long to;
    private Double fee;
    private Double rushFee;
    private Long order;
    private String status;

    @Id
    @Column(name = "service_fee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getServiceFeeId() {
        return serviceFeeId;
    }

    public void setServiceFeeId(Long serviceFeeId) {
        this.serviceFeeId = serviceFeeId;
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
    @Column(name = "from")
    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    @Basic
    @Column(name = "to")
    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    @Basic
    @Column(name = "fee")
    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    @Basic
    @Column(name = "rush_fee")
    public Double getRushFee() {
        return rushFee;
    }

    public void setRushFee(Double rushFee) {
        this.rushFee = rushFee;
    }

    @Basic
    @Column(name = "order")
    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
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
        ServiceFeeLargeEntity that = (ServiceFeeLargeEntity) o;
        return Objects.equals(serviceFeeId, that.serviceFeeId) &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(from, that.from) &&
                Objects.equals(to, that.to) &&
                Objects.equals(fee, that.fee) &&
                Objects.equals(rushFee, that.rushFee) &&
                Objects.equals(order, that.order) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceFeeId, serviceId, from, to, fee, rushFee, order, status);
    }
}
