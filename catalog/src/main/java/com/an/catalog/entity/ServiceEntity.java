package com.an.catalog.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "service", schema = "an_service", catalog = "")
public class ServiceEntity {
    private Long serviceId;
    private String serviceName;
    private String status;
    private Long serviceGroupId;

    @Id
    @Column(name = "service_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Basic
    @Column(name = "service_name")
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "service_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getServiceGroupId() {
        return serviceGroupId;
    }

    public void setServiceGroupId(Long serviceGroupId) {
        this.serviceGroupId = serviceGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceEntity that = (ServiceEntity) o;
        return Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(serviceName, that.serviceName) &&
                Objects.equals(status, that.status) &&
                Objects.equals(serviceGroupId, that.serviceGroupId)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, serviceName, status, serviceGroupId);
    }
}
