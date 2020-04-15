package com.an.catalog.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user_promotion", schema = "an_service", catalog = "")
public class UserPromotionEntity {
    private Long userPromotionId;
    private Long userId;
    private Long promotionId;
    private Date startDatetime;
    private Date endDatetime;
    private String status;
    private Long times;

    @Id
    @Column(name = "user_promotion_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getUserPromotionId() {
        return userPromotionId;
    }

    public void setUserPromotionId(Long userPromotionId) {
        this.userPromotionId = userPromotionId;
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
    @Column(name = "promotion_id")
    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
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
    @Column(name = "times")
    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPromotionEntity that = (UserPromotionEntity) o;
        return Objects.equals(userPromotionId, that.userPromotionId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(promotionId, that.promotionId) &&
                Objects.equals(startDatetime, that.startDatetime) &&
                Objects.equals(endDatetime, that.endDatetime) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userPromotionId, userId, promotionId, startDatetime, endDatetime, status, times);
    }
}
