package com.an.catalog.entity;


import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "product", schema = "an_service", catalog = "")
public class ProductEntity {
    private Long productId;
    private String productName;
    private Long productFee;
    private String productType;
    private Long discount;
    private String status;
    private Date createDatetime;
    private Date updateDatetime;
    private Long serviceId;
    private Long startMoney;

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "product_name")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Basic
    @Column(name = "product_fee")
    public Long getProductFee() {
        return productFee;
    }

    public void setProductFee(Long productFee) {
        this.productFee = productFee;
    }

    @Basic
    @Column(name = "product_type")
    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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
    @Column(name = "service_id")
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Basic
    @Column(name = "start_money")
    public Long getStartMoney() {
        return startMoney;
    }

    public void setStartMoney(Long startMoney) {
        this.startMoney = startMoney;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return productId == that.productId &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(productFee, that.productFee) &&
                Objects.equals(productType, that.productType) &&
                Objects.equals(discount, that.discount) &&
                Objects.equals(status, that.status) &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(startMoney, that.startMoney) &&
                Objects.equals(createDatetime, that.createDatetime) &&
                Objects.equals(updateDatetime, that.updateDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, productFee, productType, discount, status, serviceId, startMoney, createDatetime, updateDatetime);
    }
}
