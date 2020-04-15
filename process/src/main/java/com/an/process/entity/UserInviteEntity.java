package com.an.process.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user_invite", schema = "an_service", catalog = "")
public class UserInviteEntity {
    private Long userInviteId;
    private Long userId;
    private String inviteCode;
    private String status;
    private Date insertDatetime;

    @Id
    @Column(name = "user_invite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getUserInviteId() {
        return userInviteId;
    }

    public void setUserInviteId(Long userInviteId) {
        this.userInviteId = userInviteId;
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
    @Column(name = "invite_code")
    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
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
    @Column(name = "insert_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getInsertDatetime() {
        return insertDatetime;
    }

    public void setInsertDatetime(Date insertDatetime) {
        this.insertDatetime = insertDatetime;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInviteEntity that = (UserInviteEntity) o;
        return Objects.equals(userInviteId, that.userInviteId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(inviteCode, that.inviteCode) &&
                Objects.equals(status, that.status) &&
                Objects.equals(insertDatetime, that.insertDatetime)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userInviteId, userId, inviteCode, status, insertDatetime);
    }
}
