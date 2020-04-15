package com.an.booking.service;

import com.an.booking.client.CatalogService;
import com.an.booking.fcm.FCMService;
import com.an.common.bean.*;
import com.an.common.exception.LogicException;
import com.an.common.logger.LogsActivityAnnotation;
import com.an.common.swagger.SwaggerDocument;
import com.an.common.utils.Const;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SwaggerDocument(name = "BOOKING_API")
@Api(value = "Booking management service", description = "Endpoints for handling booking management flow")
public class ApiService {

    private static Logger logger = LoggerFactory.getLogger(ApiService.class);

    @Autowired
    BookingService bookingService;

    @Autowired
    BookingDetailService bookingDetailService;

    @Autowired
    BookingIssueService bookingIssueService;

    @Autowired
    BookingRatingService bookingRatingService;

    @Autowired
    DriverCancelService driverCancelService;

    @Autowired
    DriverOffService driverOffService;

    @Autowired
    com.an.booking.client.UserService userService;

    @Autowired
    CatalogService catalogService;

    @Autowired
    FCMService fcmService;

    @PostMapping("/api/booking")
    @LogsActivityAnnotation
    @ApiOperation("function to booking")
    public MsgWrapper booking (@ApiParam(value = "Booking object", required = true) @RequestBody Booking booking) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(booking)){
            if (Objects.equals(Const.SERVICE.TRIP, booking.getServiceId()) && StringUtils.isEmpty(booking.getProvince())){
                return new MsgWrapper(Const.WS.NOK, "With Trip service, you must input province");
            } else if (Objects.equals(Const.SERVICE.SHARE_BIKE, booking.getServiceId()) && (Objects.isNull(booking.getLstDriverSchedule()) || booking.getLstDriverSchedule().isEmpty())){
                return new MsgWrapper(Const.WS.NOK, "With Share service, you must choose driver schedule");
            }
            List<Booking> lst = bookingService.getOngoingByCustomer(booking.getCustId());
            if (lst != null && !lst.isEmpty()){
                return new MsgWrapper(Const.WS.NOK, "the customer is ongoing another trip");
            }
            if (Objects.nonNull(booking.getUserPromotionId()) && booking.getUserPromotionId() > 0L){
                MsgWrapper<UserPromotion> msgWrap = catalogService.getUserPromotionById(booking.getUserPromotionId());
                if (Objects.nonNull(msgWrap) && Objects.nonNull(msgWrap.getWrapper())){
                    UserPromotion userPromotion = msgWrap.getWrapper();
                    if (Objects.isNull(userPromotion.getTimes()) || userPromotion.getTimes() <= 0L){
                        return new MsgWrapper(Const.WS.NOK, "the promotion (times) is invalid");
                    }
                } else {
                    return new MsgWrapper(Const.WS.NOK, "the promotion is invalid");
                }
            }
            Booking output = bookingService.booking(booking);
            msgWrapper = new MsgWrapper(output, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/updateDriver")
    @LogsActivityAnnotation
    @ApiOperation("function to update driver")
    public MsgWrapper updateDriver (@ApiParam(value = "bookingId", required = true) @RequestParam("bookingId") Long bookingId, @ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(bookingId) && Objects.nonNull(driverId)){
            try {
                Booking output = bookingService.updateDriver(bookingId, driverId);
                msgWrapper = new MsgWrapper(output, Const.WS.OK, "");
            } catch (Exception ex){
                if (ex instanceof LogicException){
                    msgWrapper = new MsgWrapper(Const.WS.NOK, ex.getMessage());
                } else {
                    throw ex;
                }
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/cancelBooking")
    @LogsActivityAnnotation
    @ApiOperation("function to cancel booking")
    public MsgWrapper cancelBooking (@ApiParam(value = "bookingId", required = true) @RequestParam("bookingId") Long bookingId,
                                     @ApiParam(value = "cancelUserId", required = true) @RequestParam("cancelUserId") Long cancelUserId,
                                     @ApiParam(value = "userType", required = true) @RequestParam("userType") String userType,
                                     @ApiParam(value = "reason", required = true) @RequestParam("reason") String reason
                                     ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(bookingId) && Objects.nonNull(cancelUserId) && Objects.nonNull(userType) && Objects.nonNull(reason)){
            try {
                bookingService.cancelBooking(bookingId, cancelUserId, userType, reason);
                msgWrapper = new MsgWrapper(Const.WS.OK, "");
            } catch(Exception ex){
                if (ex instanceof LogicException){
                    msgWrapper = new MsgWrapper(Const.WS.NOK, ex.getMessage());
                } else {
                    throw ex;
                }
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/refuseBookingByDriver")
    @LogsActivityAnnotation
    @ApiOperation("function to driver refuse booking")
    public MsgWrapper refuseBookingByDriver (@ApiParam(value = "bookingId", required = true) @RequestParam("bookingId") Long bookingId,
                                             @ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(bookingId) && Objects.nonNull(driverId)){
            DriverCancel driverCancel = driverCancelService.updateDriverCancelForBooking(bookingId, driverId);
            msgWrapper = new MsgWrapper(driverCancel, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/updateOngoingBooking")
    @LogsActivityAnnotation
    @ApiOperation("function to update ongoing booking")
    public MsgWrapper updateOngoingBooking (@ApiParam(value = "bookingId", required = true) @RequestParam("bookingId") Long bookingId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(bookingId)){
            try {
                bookingService.updateOngoingBooking(bookingId);
                msgWrapper = new MsgWrapper(Const.WS.OK, "");
            } catch (Exception ex){
                msgWrapper = new MsgWrapper(Const.WS.NOK, ex.getMessage());
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/catchCustomer")
    @LogsActivityAnnotation
    @ApiOperation("function to push notification to customer when driver catch customer")
    public MsgWrapper catchCustomer (@ApiParam(value = "bookingId", required = true) @RequestParam("bookingId") Long bookingId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(bookingId)){
            try {
                Booking booking = bookingService.findById(bookingId);
                if (Objects.nonNull(booking)){
                    // push notification for customer
                    String token = "";
                    MsgWrapper<User> msg = userService.getUserById(booking.getCustId());
                    if (Objects.nonNull(msg) && Objects.nonNull(msg.getWrapper())){
                        token = msg.getWrapper().getFcmToken();
                    }
                    if (!StringUtils.isEmpty(token)) {
                        Map<String, String> map = new HashMap<>();
                        map.put("action", "3");
                        List<Long> lstShareService = Arrays.asList(Const.SERVICE.SHARE_BIKE, Const.SERVICE.SHARE_CAR, Const.SERVICE.SHARE_CAR7);
                        if (lstShareService.contains(booking.getServiceId())){
                            FcmBean fcmBean = FcmBean.createFcmBean(booking);
                            MsgWrapper<User> driverMsg = userService.getUserById(booking.getDriverId());
                            if (Objects.nonNull(driverMsg) && Objects.nonNull(driverMsg.getWrapper())) {
                                fcmBean.setDriverId(driverMsg.getWrapper().getUserId());
                                fcmBean.setDriverMobile(driverMsg.getWrapper().getMobile());
                                fcmBean.setDriverName(driverMsg.getWrapper().getFullname());
                                fcmBean.setDriverRating(driverMsg.getWrapper().getRating());
                                List<UserService> lst = driverMsg.getWrapper().getLstUserService();
                                if (lst != null && !lst.isEmpty()) {
                                    lst.forEach(x-> {
                                        if (Objects.equals(x.getServiceId(), booking.getServiceId())) {
                                            fcmBean.setVehicleNo(x.getVehicleNo());
                                            fcmBean.setVehicleType(x.getVehicleType());
                                        }
                                    });

                                }
                            }

                            // get driver location
                            try {
                                MsgWrapper<com.an.common.bean.Service> msgWrapp = catalogService.getServiceById(booking.getServiceId());
                                if (Objects.nonNull(msgWrapp) && Objects.nonNull(msgWrapp.getWrapper())){
                                    MsgWrapper<UserLocation> msgWrapper1 = userService.getDriverLocation(booking.getDriverId(), msgWrapp.getWrapper().getServiceGroupId());
                                    if (Objects.nonNull(msgWrapper1) && Objects.nonNull(msgWrapper1.getWrapper())){
                                        fcmBean.setDriverX(msgWrapper1.getWrapper().getX());
                                        fcmBean.setDriverY(msgWrapper1.getWrapper().getY());
                                    }
                                }
                            } catch (Exception ex){
                                logger.error(ex.getMessage(), ex);
                            }
                            map.put("data", new Gson().toJson(fcmBean));
                        } else {
                            map.put("data", new Gson().toJson(booking.getBookingId()));
                        }
                        fcmService.sendMessageToToken(new PushNotificationRequest("Catch customer", "", "catchCustomer", token, map)); // gson.toJson(entity)
                        msgWrapper = new MsgWrapper(Const.WS.OK, "");
                    } else {
                        msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid cancel user");
                    }
                } else {
                    msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid booking");
                }
            } catch (Exception ex){
                msgWrapper = new MsgWrapper(Const.WS.NOK, ex.getMessage());
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/finishBooking")
    @LogsActivityAnnotation
    @ApiOperation("function to finish booking for driver")
    public MsgWrapper finishBooking (@ApiParam(value = "bookingId", required = true) @RequestParam("bookingId") Long bookingId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(bookingId)){
            try {
                bookingService.finishBooking(bookingId);
                msgWrapper = new MsgWrapper(Const.WS.OK, "");

            } catch (Exception ex){
                msgWrapper = new MsgWrapper(Const.WS.NOK, ex.getMessage());
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/confirmPayment")
    @LogsActivityAnnotation
    @ApiOperation("function to confirm payment for driver")
    public MsgWrapper confirmPayment (@ApiParam(value = "bookingId", required = true) @RequestParam("bookingId") Long bookingId,
                                      @ApiParam(value = "streetFee", required = false) @RequestParam(value = "streetFee", required = false) Long streetFee
                                      ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(bookingId)){
            try {
                msgWrapper = new MsgWrapper(bookingService.confirmPayment(bookingId, streetFee), Const.WS.OK, "");
            } catch (Exception ex){
                msgWrapper = new MsgWrapper(Const.WS.NOK, ex.getMessage());
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/api/getBookingIssue")
    @LogsActivityAnnotation
    @ApiOperation("function to get booking issue by booking id")
    public MsgWrapper getBookingIssue (@ApiParam(value = "bookingId", required = true) @RequestParam("bookingId") Long bookingId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(bookingId)){
            List<BookingIssue> lstBookingIssue = bookingIssueService.findByBookingId(bookingId);
            msgWrapper = new MsgWrapper(lstBookingIssue, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/api/getBookingRating")
    @LogsActivityAnnotation
    @ApiOperation("function to get booking rating by booking id")
    public MsgWrapper getBookingRating (@ApiParam(value = "bookingId", required = true) @RequestParam("bookingId") Long bookingId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(bookingId)){
            BookingRating bookingRating = bookingRatingService.findByBookingId(bookingId);
            msgWrapper = new MsgWrapper(bookingRating, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/rating")
    @LogsActivityAnnotation
    @ApiOperation("function to rating driver")
    public MsgWrapper rating (@ApiParam(value = "bookingId", required = true) @RequestParam("bookingId") Long bookingId,
                              @ApiParam(value = "rating", required = true) @RequestParam("rating") Double rating,
                              @ApiParam(value = "rating note", required = false) @RequestParam(value = "ratingNote", required = false) String ratingNote
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(bookingId)){
            Booking booking = bookingService.findById(bookingId);
            if (Objects.nonNull(booking)) {
                BookingRating bookingRating = bookingRatingService.createBookingRating(bookingId, booking.getDriverId(), booking.getCustId(), ratingNote, rating);
                msgWrapper = new MsgWrapper(bookingRating, Const.WS.OK, "");
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid booking id");
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/api/getDriverRating")
    @LogsActivityAnnotation
    @ApiOperation("function to get booking rating by driver id")
    public MsgWrapper getDriverRating (@ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(driverId)){
            List<BookingRating> lst = bookingRatingService.findByDriverId(driverId);
            msgWrapper = new MsgWrapper(lst, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/api/getBookingStatus")
    @LogsActivityAnnotation
    @ApiOperation("function to get booking status by booking id")
    public MsgWrapper getBookingStatus (@ApiParam(value = "bookingId", required = true) @RequestParam("bookingId") Long bookingId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(bookingId)){
            Booking booking = bookingService.findById(bookingId);
            if (Objects.nonNull(booking)) {
                msgWrapper = new MsgWrapper(booking.getStatus(), Const.WS.OK, "");
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid booking");
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/onOffDriver")
    @LogsActivityAnnotation
    @ApiOperation("function to update driver status")
    public MsgWrapper onOffDriver(@ApiParam(value = "serviceGroupId: 1 - Bike | 2 - Car | 3 - Car 7 | 4 - Truck", required = true) @RequestParam("serviceGroupId") Long serviceGroupId,
                                  @ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId,
                                  @ApiParam(value = "onOff: 1 - on | 0 - off", required = true) @RequestParam("onOff") String onOff
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(driverId) && Objects.nonNull(onOff)) {
            if (Objects.equals(Const.DRIVER_ON_OFF.ON, onOff)) {
                driverOffService.evictDriver(serviceGroupId, driverId);
            } else if (Objects.equals(Const.DRIVER_ON_OFF.OFF, onOff)) {
                driverOffService.insertDriver(serviceGroupId, driverId);
            }
            msgWrapper = new MsgWrapper(Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/api/getBookingReportByDay")
    @LogsActivityAnnotation
    @ApiOperation("function to get booking report by day")
    public MsgWrapper getBookingReportByDay(@ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(driverId)) {
            List<Booking> lst = bookingService.getBookingReportByDay(driverId);
            if (lst != null && !lst.isEmpty()){
                lst.forEach(x->{
                    try {
                        MsgWrapper<UserService> msg = userService.findUserService(x.getDriverId(), x.getServiceId());
                        if (Objects.nonNull(msg) && Objects.nonNull(msg.getWrapper())){
                            UserService userService = msg.getWrapper();
                            if (Objects.nonNull(userService.getExpireDatetime())) {
                                x.setRemainDays(ChronoUnit.DAYS.between(LocalDate.now(), new Date(userService.getExpireDatetime().getTime()).toLocalDate()));
                            } else {
                                x.setRemainDays(1000L);
                            }
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                });
            }
            msgWrapper = new MsgWrapper(lst, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/api/getUnfinishBookingByDriverIdAndServiceIdOnNowDate")
    @LogsActivityAnnotation
    @ApiOperation("function to get booking by driver, service and current date")
    public MsgWrapper getUnfinishBookingByDriverIdAndServiceIdOnNowDate(@ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId,
                                                                        @ApiParam(value = "serviceId", required = true) @RequestParam("serviceId") Long serviceId) throws Exception {
        return new MsgWrapper(bookingService.getUnfinishBookingByDriverIdAndServiceIdOnNowDate(driverId, serviceId), Const.WS.OK, "");
    }
}
