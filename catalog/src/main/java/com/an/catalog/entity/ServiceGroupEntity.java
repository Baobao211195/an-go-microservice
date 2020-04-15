package com.an.catalog.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "service_group", schema = "an_service", catalog = "")
public class ServiceGroupEntity {
    private Long serviceGroupId;
    private String code;
    private String name;
    private String status;

    @Id
    @Column(name = "service_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getServiceGroupId() {
        return serviceGroupId;
    }

    public void setServiceGroupId(Long serviceGroupId) {
        this.serviceGroupId = serviceGroupId;
    }

    @Basic
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        ServiceGroupEntity that = (ServiceGroupEntity) o;
        return Objects.equals(serviceGroupId, that.serviceGroupId) &&
                Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceGroupId, code, name, status);
    }
}
