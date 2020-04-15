package com.an.booking.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "driver_schedule", schema = "an_service", catalog = "")
public class DriverScheduleEntity {
    private Long driverScheduleId;
    private Date scheduleDate;
    private Long fromHour;
    private Long fromMinute;
    private Long toHour;
    private Long toMinute;
    private Long driverId;
    private String status;
    private Date createDatetime;
    private Date updateDatetime;
    private Long serviceId;
    private String driverSchedulePathGroupId;
    private String fromPoint;
    private String toPoint;
    private Long numOfSchedule;

    public DriverScheduleEntity() {
    }

    public DriverScheduleEntity(Long driverId, Long numOfSchedule) {
        this.driverId = driverId;
        this.numOfSchedule = numOfSchedule;
    }

    @Id
    @Column(name = "driver_schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getDriverScheduleId() {
        return driverScheduleId;
    }

    public void setDriverScheduleId(Long driverScheduleId) {
        this.driverScheduleId = driverScheduleId;
    }

    @Basic
    @Column(name = "schedule_date")
    @Temporal(TemporalType.DATE)
    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    @Basic
    @Column(name = "from_hour")
    public Long getFromHour() {
        return fromHour;
    }

    public void setFromHour(Long fromHour) {
        this.fromHour = fromHour;
    }

    @Basic
    @Column(name = "from_minute")
    public Long getFromMinute() {
        return fromMinute;
    }

    public void setFromMinute(Long fromMinute) {
        this.fromMinute = fromMinute;
    }

    @Basic
    @Column(name = "to_hour")
    public Long getToHour() {
        return toHour;
    }

    public void setToHour(Long toHour) {
        this.toHour = toHour;
    }

    @Basic
    @Column(name = "to_minute")
    public Long getToMinute() {
        return toMinute;
    }

    public void setToMinute(Long toMinute) {
        this.toMinute = toMinute;
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
    @Column(name = "driver_schedule_path_group_id")
    public String getDriverSchedulePathGroupId() {
        return driverSchedulePathGroupId;
    }

    public void setDriverSchedulePathGroupId(String driverSchedulePathGroupId) {
        this.driverSchedulePathGroupId = driverSchedulePathGroupId;
    }

    @Basic
    @Column(name = "from_point")
    public String getFromPoint() {
        return fromPoint;
    }

    public void setFromPoint(String fromPoint) {
        this.fromPoint = fromPoint;
    }

    @Basic
    @Column(name = "to_point")
    public String getToPoint() {
        return toPoint;
    }

    public void setToPoint(String toPoint) {
        this.toPoint = toPoint;
    }

    @Transient
    public Long getNumOfSchedule() {
        return numOfSchedule;
    }

    public void setNumOfSchedule(Long numOfSchedule) {
        this.numOfSchedule = numOfSchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DriverScheduleEntity that = (DriverScheduleEntity) o;
        return Objects.equals(driverScheduleId, that.driverScheduleId) &&
                Objects.equals(scheduleDate, that.scheduleDate) &&
                Objects.equals(fromHour, that.fromHour) &&
                Objects.equals(fromMinute, that.fromMinute) &&
                Objects.equals(toHour, that.toHour) &&
                Objects.equals(toMinute, that.toMinute) &&
                Objects.equals(driverId, that.driverId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createDatetime, that.createDatetime) &&
                Objects.equals(updateDatetime, that.updateDatetime) &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(driverSchedulePathGroupId, that.driverSchedulePathGroupId) &&
                Objects.equals(fromPoint, that.fromPoint) &&
                Objects.equals(toPoint, that.toPoint)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverScheduleId, scheduleDate, fromHour, fromMinute, toHour, toMinute, driverId, status, createDatetime, updateDatetime, serviceId, driverSchedulePathGroupId, fromPoint, toPoint);
    }
}
