package com.an.process.thread;

import com.an.common.bean.PushNotificationRequest;
import com.an.common.bean.User;
import com.an.common.bean.UserService;
import com.an.common.utils.Const;
import com.an.process.fcm.FCMService;
import com.an.process.service.UserServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class WarningUserServiceExpireThread {
    private static Logger logger = LoggerFactory.getLogger(WarningUserServiceExpireThread.class);

    @Value("${service.expire.back.date}")
    private Long backDate;

    @Value("${service.expire.message}")
    private String expireMessage;

    @Autowired
    UserServiceService userServiceService;

    @Autowired
    FCMService fcmService;

    @Autowired
    com.an.process.service.UserService userService;

    @Scheduled(cron = "0 0 9 * * ?")
    void process(){
        List<UserService> lst = userServiceService.findByStatusAndExpireDatetimeGreaterThan(Const.USER_SERVICE.STATUS_APPROVED, Timestamp.valueOf(LocalDateTime.now().minusDays(backDate)));
        if (lst != null && !lst.isEmpty()){
            logger.info("started warning service expire: " + new Date());
            lst.forEach(x->{
                try {
                    User user = userService.findByUserId(x.getUserId());
                    if (Objects.nonNull(user)) {
                        String message = new MessageFormat(expireMessage).format(new Object[]{getServiceName(x.getServiceId()), x.getExpireDatetime()});
                        Map<String, String> map = new HashMap<>();
                        map.put("message", message);
                        fcmService.sendMessageToToken(new PushNotificationRequest("ServiceExpire", message, "ServiceExpire", user.getFcmToken(), map));
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            });
            logger.info("finished warning service expire: " + new Date());
        }
    }

    private String getServiceName(Long serviceId){
        if (Objects.equals(serviceId, Const.SERVICE.GO_BIKE)){
            return "Bike";
        } else if (Objects.equals(serviceId, Const.SERVICE.GO_CAR)){
            return "Car";
        } else if (Objects.equals(serviceId, Const.SERVICE.GO_CAR7)){
            return "Car7";
        } else if (Objects.equals(serviceId, Const.SERVICE.VAN)){
            return "Van";
        }
        return "";
    }

}
