package com.an.process.thread;

import com.an.common.bean.BookingRating;
import com.an.process.service.BookingRatingService;
import com.an.process.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class UpdateDriverRatingThread {
    private static Logger logger = LoggerFactory.getLogger(UpdateDriverRatingThread.class);

    @Autowired
    BookingRatingService bookingRatingService;

    @Autowired
    UserService userService;

    @Scheduled(cron = "0 30 0 * * ?")
    void process (){
        List<BookingRating> lst = bookingRatingService.findAvgRating();
        if (lst != null && !lst.isEmpty()){
            logger.info("started updating driver rating: " + new Date());
            lst.forEach(x->userService.updateRating(x.getDriverId(), x.getAvgRating()));
            logger.info("finished updating driver rating: " + new Date());

        }
    }
}
