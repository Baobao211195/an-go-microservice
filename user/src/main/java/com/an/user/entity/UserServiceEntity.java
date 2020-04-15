package com.an.user.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user_service", schema = "an_service", catalog = "")
public class UserServiceEntity {
    private Long userServiceId;
    private Long userId;
    private Long serviceId;
    private Long productId;
    private String vehicleNo;
    private String vehicleType;
    private String status;
    private Date createDatetime;
    private Date updateDatetime;
    private Date expireDatetime;
    private String description;

    @Id
    @Column(name = "user_service_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getUserServiceId() {
        return userServiceId;
    }

    public void setUserServiceId(Long userServiceId) {
        this.userServiceId = userServiceId;
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
    @Column(name = "service_id")
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Basic
    @Column(name = "product_id")
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "vehicle_no")
    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    @Basic
    @Column(name = "vehicle_type")
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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
    @Column(name = "create_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    @Basic
    @Column(name = "update_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Basic
    @Column(name = "expire_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getExpireDatetime() {
        return expireDatetime;
    }

    public void setExpireDatetime(Date expireDatetime) {
        this.expireDatetime = expireDatetime;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserServiceEntity that = (UserServiceEntity) o;
        return userServiceId == that.userServiceId &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(vehicleNo, that.vehicleNo) &&
                Objects.equals(vehicleType, that.vehicleType) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createDatetime, that.createDatetime) &&
                Objects.equals(updateDatetime, that.updateDatetime) &&
                Objects.equals(expireDatetime, that.expireDatetime) &&
                Objects.equals(description, that.description)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userServiceId, userId, serviceId, productId, vehicleNo, vehicleType, status, createDatetime, updateDatetime, expireDatetime, description);
    }
}
