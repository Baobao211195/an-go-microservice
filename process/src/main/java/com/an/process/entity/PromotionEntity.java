package com.an.process.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "promotion", schema = "an_service", catalog = "")
public class PromotionEntity {
    private Long promotionId;
    private String name;
    private String content;
    private String type;
    private Long value;
    private Long maxValue;
    private Date startDatetime;
    private Date endDatetime;
    private String status;
    private Long serviceId;

    @Id
    @Column(name = "promotion_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
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
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "value")
    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Basic
    @Column(name = "max_value")
    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
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
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "service_id")
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PromotionEntity that = (PromotionEntity) o;
        return promotionId == that.promotionId &&
                Objects.equals(name, that.name) &&
                Objects.equals(content, that.content) &&
                Objects.equals(type, that.type) &&
                Objects.equals(value, that.value) &&
                Objects.equals(maxValue, that.maxValue) &&
                Objects.equals(startDatetime, that.startDatetime) &&
                Objects.equals(endDatetime, that.endDatetime) &&
                Objects.equals(status, that.status) &&
                Objects.equals(serviceId, that.serviceId) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(promotionId, name, content, type, value, maxValue, startDatetime, endDatetime, status, serviceId);
    }
}
