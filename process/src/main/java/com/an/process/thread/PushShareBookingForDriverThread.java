package com.an.process.thread;

import com.an.common.bean.Booking;
import com.an.common.bean.DriverSchedule;
import com.an.common.bean.PushNotificationRequest;
import com.an.common.bean.User;
import com.an.common.utils.Const;
import com.an.process.fcm.FCMService;
import com.an.process.service.BookingService;
import com.an.process.service.DriverScheduleService;
import com.an.process.service.UserService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PushShareBookingForDriverThread {

    private static Logger logger = LoggerFactory.getLogger(PushShareBookingForDriverThread.class);
    private static Gson gson = new Gson();

    @Autowired
    BookingService bookingService;

    @Autowired
    DriverScheduleService driverScheduleService;

    @Autowired
    UserService userService;

    @Autowired
    FCMService fcmService;

    @Scheduled(cron = "0 * * * * ?")
    void process(){
        List<DriverSchedule> lst = driverScheduleService.findByScheduleDateAndStatus();
        if (lst != null && !lst.isEmpty()){
            logger.info("started push share booking for driver: " + new Date());
            lst.forEach(x->{
                List<Booking> lstBookings = bookingService.findByDriverScheduleIdAndStatus(x.getDriverScheduleId(), Const.BOOKING.STATUS_QUEUE);
                if (lstBookings != null && !lstBookings.isEmpty()) {
                    x.setLstBooking(lstBookings);
                    // get fcm token
                    User user = userService.findByUserId(x.getDriverId());

                    if (Objects.nonNull(user)) {
                        // push notification to driver
                        Map<String, String> map = new HashMap<>();
                        map.put("data", gson.toJson(x));
                        logger.info("fcmBean: " + gson.toJson(x));
                        logger.info("fcmToken: " + user.getFcmToken());
                        try {
                            fcmService.sendMessageToToken(new PushNotificationRequest("ShareBooking", "", "booking", user.getFcmToken(), map)); // gson.toJson(fcmBean)
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
                // update driver schedule status
                driverScheduleService.updateDriverScheduleStatus(x.getDriverScheduleId(), Const.DRIVER_SCHEDULE.STATUS_PROCESSED);
            });
            logger.info("finished push share booking for driver: " + new Date());
        }
    }

}
