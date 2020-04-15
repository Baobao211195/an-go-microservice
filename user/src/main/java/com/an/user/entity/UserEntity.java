package com.an.user.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "an_service", catalog = "")
public class UserEntity {
    private Long userId;
    private String fullname;
    private String mobile;
    private String email;
    private String password;
    private Date createDatetime;
    private Date updateDatetime;
    private Double x;
    private Double y;
    private String status;
    private String avatar;
    private String type;
    private Double rating;
    private String socialId;
    private String idNoFront;
    private String idNoBack;
    private String fcmToken;
    private String province;
    private String inviteCode;

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "fullname")
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Basic
    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "avatar")
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
    @Column(name = "rating")
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Basic
    @Column(name = "social_id")
    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    @Basic
    @Column(name = "id_no_front")
    public String getIdNoFront() {
        return idNoFront;
    }

    public void setIdNoFront(String idNoFront) {
        this.idNoFront = idNoFront;
    }

    @Basic
    @Column(name = "id_no_back")
    public String getIdNoBack() {
        return idNoBack;
    }

    public void setIdNoBack(String idNoBack) {
        this.idNoBack = idNoBack;
    }

    @Basic
    @Column(name = "fcm_token")
    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Basic
    @Column(name = "province")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Basic
    @Column(name = "invite_code")
    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return userId == that.userId &&
                Objects.equals(fullname, that.fullname) &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(createDatetime, that.createDatetime) &&
                Objects.equals(updateDatetime, that.updateDatetime) &&
                Objects.equals(x, that.x) &&
                Objects.equals(y, that.y) &&
                Objects.equals(status, that.status) &&
                Objects.equals(avatar, that.avatar) &&
                Objects.equals(type, that.type) &&
                Objects.equals(rating, that.rating) &&
                Objects.equals(socialId, that.socialId) &&
                Objects.equals(idNoFront, that.idNoFront) &&
                Objects.equals(idNoBack, that.idNoBack) &&
                Objects.equals(fcmToken, that.fcmToken) &&
                Objects.equals(province, that.province) &&
                Objects.equals(inviteCode, that.inviteCode)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, fullname, mobile, email, password, createDatetime, updateDatetime, x, y, status, avatar, type, rating, socialId, idNoFront, idNoBack, fcmToken, province, inviteCode);
    }
}
