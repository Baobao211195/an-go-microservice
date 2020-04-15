package com.an.catalog.service;

import com.an.common.bean.Notification;
import com.an.common.exception.LogicException;

import java.util.Date;
import java.util.List;

public interface NotificationService {

    List<Notification> findByStatus(String status, Date createDatetime);

    Notification createNotification(String name, String content, String type, Date pushDatetime);

    Notification updateNotification(Long notificationId, String name, String content, String type, Date pushDatetime, String status) throws LogicException, Exception;
}
