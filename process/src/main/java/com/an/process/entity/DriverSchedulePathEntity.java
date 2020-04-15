package com.an.process.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "driver_schedule_path", schema = "an_service", catalog = "")
public class DriverSchedulePathEntity {
    private Long driverSchedulePathId;
    private String driverSchedulePathGroupId;
    private Double x;
    private Double y;
    private Long orderNumber;
    private String status;

    @Id
    @Column(name = "driver_schedule_path_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getDriverSchedulePathId() {
        return driverSchedulePathId;
    }

    public void setDriverSchedulePathId(Long driverSchedulePathId) {
        this.driverSchedulePathId = driverSchedulePathId;
    }

    @Basic
    @Column(name = "driver_schedule_path_group_id")
    public String getDriverSchedulePathGroupId() {
        return driverSchedulePathGroupId;
    }

    public void setDriverSchedulePathGroupId(String driverSchedulePathGroupId) {
        this.driverSchedulePathGroupId = driverSchedulePathGroupId;
    }

    @Basic
    @Column(name = "x")
    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    @Basic
    @Column(name = "y")
    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Basic
    @Column(name = "order_number")
    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
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
        DriverSchedulePathEntity that = (DriverSchedulePathEntity) o;
        return Objects.equals(driverSchedulePathId, that.driverSchedulePathId) &&
                Objects.equals(driverSchedulePathGroupId, that.driverSchedulePathGroupId) &&
                Objects.equals(x, that.x) &&
                Objects.equals(y, that.y) &&
                Objects.equals(orderNumber, that.orderNumber) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverSchedulePathId, driverSchedulePathGroupId, x, y, orderNumber, status);
    }
}
