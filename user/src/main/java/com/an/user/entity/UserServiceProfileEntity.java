package com.an.user.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_service_profile", schema = "an_service", catalog = "")
public class UserServiceProfileEntity {
    private Long userServiceProfileId;
    private Long userServiceId;
    private String profileType;
    private String profileValue;
    private String status;

    @Id
    @Column(name = "user_service_profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getUserServiceProfileId() {
        return userServiceProfileId;
    }

    public void setUserServiceProfileId(Long userServiceProfileId) {
        this.userServiceProfileId = userServiceProfileId;
    }

    @Basic
    @Column(name = "user_service_id")
    public Long getUserServiceId() {
        return userServiceId;
    }

    public void setUserServiceId(Long userId) {
        this.userServiceId = userId;
    }

    @Basic
    @Column(name = "profile_type")
    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    @Basic
    @Column(name = "profile_value")
    public String getProfileValue() {
        return profileValue;
    }

    public void setProfileValue(String profileValue) {
        this.profileValue = profileValue;
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
        UserServiceProfileEntity that = (UserServiceProfileEntity) o;
        return userServiceProfileId == that.userServiceProfileId &&
                userServiceId == that.userServiceId &&
                Objects.equals(profileType, that.profileType) &&
                Objects.equals(profileValue, that.profileValue) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userServiceProfileId, userServiceId, profileType, profileValue, status);
    }
}
