package com.an.catalog.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "daily_title", schema = "an_service", catalog = "")
public class DailyTitleEntity {
    private Long dailyTitleId;
    private String content;
    private Date showDatetime;

    @Id
    @Column(name = "daily_title_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getDailyTitleId() {
        return dailyTitleId;
    }

    public void setDailyTitleId(Long dailyTitleId) {
        this.dailyTitleId = dailyTitleId;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "show_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getShowDatetime() {
        return showDatetime;
    }

    public void setShowDatetime(Date showDatetime) {
        this.showDatetime = showDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyTitleEntity that = (DailyTitleEntity) o;
        return Objects.equals(dailyTitleId, that.dailyTitleId) &&
                Objects.equals(content, that.content) &&
                Objects.equals(showDatetime, that.showDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dailyTitleId, content, showDatetime);
    }
}
