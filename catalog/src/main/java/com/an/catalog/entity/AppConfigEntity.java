package com.an.catalog.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "app_config", schema = "an_service", catalog = "")
public class AppConfigEntity {
    private Long appConfigId;
    private String configCode;
    private String configName;
    private String configValue;
    private String status;

    @Id
    @Column(name = "app_config_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getAppConfigId() {
        return appConfigId;
    }

    public void setAppConfigId(Long appConfigId) {
        this.appConfigId = appConfigId;
    }

    @Basic
    @Column(name = "config_code")
    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    @Basic
    @Column(name = "config_name")
    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @Basic
    @Column(name = "config_value")
    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
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
        AppConfigEntity that = (AppConfigEntity) o;
        return Objects.equals(appConfigId, that.appConfigId) &&
                Objects.equals(configCode, that.configCode) &&
                Objects.equals(configName, that.configName) &&
                Objects.equals(configValue, that.configValue) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appConfigId, configCode, configName, configValue, status);
    }
}
