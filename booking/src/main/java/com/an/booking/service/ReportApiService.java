package com.an.booking.service;

import com.an.common.bean.Booking;
import com.an.common.bean.MsgWrapper;
import com.an.common.logger.LogsActivityAnnotation;
import com.an.common.swagger.SwaggerDocument;
import com.an.common.utils.Const;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SwaggerDocument(name = "REPORT_SERVICE_API")
@Api(value = "Report management service", description = "Endpoints for handling report management flow")
public class ReportApiService {
    private static Logger logger = LoggerFactory.getLogger(ReportApiService.class);

    @Autowired
    BookingService bookingService;

    @GetMapping("/api/getBookingHistory")
    @LogsActivityAnnotation
    @ApiOperation("function to get booking history by custId/driverId")
    public MsgWrapper getBookingHistory (@ApiParam(value = "userId", required = true) @RequestParam("userId") Long userId,
                                         @ApiParam(value = "userType: 0 - customer | 1 - driver", required = true) @RequestParam("userType") String userType
                                         ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userId)){
            List<Booking> lstBooking;
            if (Objects.equals(Const.USER.TYPE_CUSTOMER, userType)){
                lstBooking = bookingService.findBookingHistoryByCustId(userId);
            } else {
                lstBooking = bookingService.findBookingHistoryByDriverId(userId);
            }
            msgWrapper = new MsgWrapper(lstBooking, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }
}
