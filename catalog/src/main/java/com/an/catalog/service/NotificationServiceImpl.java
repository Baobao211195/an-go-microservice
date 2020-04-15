package com.an.catalog.service;

import com.an.catalog.entity.NotificationEntity;
import com.an.catalog.repository.NotificationRepository;
import com.an.common.bean.Notification;
import com.an.common.exception.LogicException;
import com.an.common.utils.Const;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    NotificationRepository repository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public List<Notification> findByStatus(String status, Date createDatetime) {
        List<Notification> output = new ArrayList<>();
        List<NotificationEntity> lst = repository.findAllByStatusAndCreateDatetimeGreaterThan(status, createDatetime);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x,Notification.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Notification createNotification(String name, String content, String type, Date pushDatetime) {
        NotificationEntity notification = new NotificationEntity();
        notification.setName(name);
        notification.setContent(content);
        notification.setType(type);
        notification.setPushDatetime(pushDatetime);
        notification.setStatus(Const.NOTIFICATION.STATUS_ON);
        notification.setCreateDatetime(repository.getSysdate());
        notification = repository.save(notification);
        if (Objects.nonNull(notification)){
            return modelMapper.map(notification, Notification.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Notification updateNotification(Long notificationId, String name, String content, String type, Date pushDatetime, String status) throws LogicException, Exception {
        Optional<NotificationEntity> optional = repository.findById(notificationId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            NotificationEntity entity = optional.get();
            if (!Objects.equals(name, entity.getName())){
                entity.setName(name);
            }
            if (!Objects.equals(content, entity.getContent())){
                entity.setContent(content);
            }
            if (!Objects.equals(type, entity.getType())){
                entity.setType(type);
            }
            if (!Objects.equals(status, entity.getStatus())){
                entity.setStatus(status);
            }
            if (!Objects.equals(pushDatetime, entity.getPushDatetime())){
                entity.setPushDatetime(pushDatetime);
            }
            entity.setUpdateDatetime(repository.getSysdate());
            entity = repository.save(entity);
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, Notification.class);
            }
        } else {
            throw new LogicException(Const.WS.NOK, "invalid notification id");
        }
        return null;
    }
}
