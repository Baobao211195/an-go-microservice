package com.an.catalog.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "service_fee", schema = "an_service", catalog = "")
public class ServiceFeeEntity {
    private Long serviceFeeId;
    private Double minDistance;
    private Long minFee;
    private Long normalFee;
    private Long timeFee;
    private Long serviceId;
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
    @Column(name = "min_distance")
    public Double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(Double minDistance) {
        this.minDistance = minDistance;
    }

    @Basic
    @Column(name = "min_fee")
    public Long getMinFee() {
        return minFee;
    }

    public void setMinFee(Long minFee) {
        this.minFee = minFee;
    }

    @Basic
    @Column(name = "normal_fee")
    public Long getNormalFee() {
        return normalFee;
    }

    public void setNormalFee(Long normalFee) {
        this.normalFee = normalFee;
    }

    @Basic
    @Column(name = "time_fee")
    public Long getTimeFee() {
        return timeFee;
    }

    public void setTimeFee(Long timeFee) {
        this.timeFee = timeFee;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceFeeEntity that = (ServiceFeeEntity) o;
        return Objects.equals(serviceFeeId, that.serviceFeeId) &&
                Objects.equals(minDistance, that.minDistance) &&
                Objects.equals(minFee, that.minFee) &&
                Objects.equals(normalFee, that.normalFee) &&
                Objects.equals(timeFee, that.timeFee) &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceFeeId, minDistance, minFee, normalFee, timeFee, serviceId, status);
    }
}
