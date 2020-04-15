package com.an.booking.service;

import com.an.booking.client.CatalogService;
import com.an.booking.client.UserService;
import com.an.common.bean.*;
import com.an.common.exception.LogicException;
import com.an.common.logger.LogsActivityAnnotation;
import com.an.common.swagger.SwaggerDocument;
import com.an.common.utils.Const;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SwaggerDocument(name = "SHARE_SERVICE_API")
@Api(value = "Share management service", description = "Endpoints for handling share management flow")
public class ShareApiService {
    private static Logger logger = LoggerFactory.getLogger(ShareApiService.class);

    @Autowired
    DriverScheduleService driverScheduleService;

    @Autowired
    DriverSchedulePathService driverSchedulePathService;

    @Autowired
    BookingService bookingService;

    @Autowired
    UserService userService;

    @Autowired
    DriverOffService driverOffService;

    @Autowired
    CatalogService catalogService;

    @GetMapping("/api/findByDriverIdAndSchedule")
    @LogsActivityAnnotation
    @ApiOperation("function to find by driver and schedule")
    public MsgWrapper findByDriverIdAndSchedule(@ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId,
                                                @ApiParam(value = "serviceId", required = true) @RequestParam("serviceId") Long serviceId,
                                                @ApiParam(value = "schedule date. Formart date: ddMMyyyy", required = true) @RequestParam("scheduleDate") String scheduleDate
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(driverId) && Objects.nonNull(serviceId) && !StringUtils.isEmpty(scheduleDate)) {
            try {
                Date schedule;
                try {
                    schedule = new SimpleDateFormat("ddMMyyyy").parse(scheduleDate);
                } catch (Exception ex) {
                    return new MsgWrapper(Const.WS.NOK, "invalid schedule date format");
                }
                List<DriverSchedule> lst = driverScheduleService.findByDriverIdAndSchedule(driverId, serviceId, schedule);
                msgWrapper = new MsgWrapper(lst, Const.WS.OK, "");
            } catch (Exception ex) {
                if (ex instanceof LogicException) {
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

    @PostMapping("/api/saveListDriverSchedule")
    @LogsActivityAnnotation
    @ApiOperation("function to list driver schedule")
    public MsgWrapper saveListDriverSchedule(@ApiParam(value = "lstDriverSchedule", required = true) @RequestParam("lstDriverSchedule") List<DriverSchedule> lstDriverSchedule,
                                             @ApiParam(value = "lstDriverSchedulePath", required = true) @RequestParam("lstDriverSchedulePath") List<DriverSchedulePath> lstDriverSchedulePath
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(lstDriverSchedule) && !lstDriverSchedule.isEmpty()) {
            try {
                // validate schedule
                Long driverId = null, serviceId = null, fromHour = null, fromMinute = null, toHour = null, toMinute = null;
                List<Date> lstScheduleDate = new ArrayList<>();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                for (DriverSchedule driverSchedule : lstDriverSchedule){
                    if (Objects.isNull(driverId)){
                        driverId = driverSchedule.getDriverId();
                    }
                    if (Objects.isNull(serviceId)){
                        serviceId = driverSchedule.getServiceId();
                    }
                    if (Objects.isNull(fromHour)){
                        fromHour = driverSchedule.getFromHour();
                    }
                    if (Objects.isNull(fromMinute)){
                        fromMinute = driverSchedule.getFromMinute();
                    }
                    if (Objects.isNull(toHour)){
                        toHour = driverSchedule.getToHour();
                    }
                    if (Objects.isNull(toMinute)){
                        toMinute = driverSchedule.getToMinute();
                    }
                    try {
                        lstScheduleDate.add(simpleDateFormat.parse(driverSchedule.getStrScheduleDate()));
                    } catch (Exception ex) {}
                }

                // validate driver already registered service
                if (Objects.nonNull(driverId) && Objects.nonNull(serviceId)){
                    MsgWrapper<com.an.common.bean.UserService> msg = userService.findUserService(driverId, serviceId);
                    if (Objects.isNull(msg) || Objects.isNull(msg.getWrapper())){
                        return new MsgWrapper(Const.WS.NOK, "the driver has not registered service: " + serviceId);
                    }
                } else {
                    return new MsgWrapper(Const.WS.NOK, "invalid parameter");
                }

                List<DriverSchedule> lst = driverScheduleService.findByDriverIdAndServiceIdAndListScheduleDateAndTime(driverId, serviceId, lstScheduleDate, fromHour, fromMinute, toHour, toMinute);
                if (lst != null && !lst.isEmpty()){
                    return new MsgWrapper(Const.WS.NOK, "the driver schedule has been duplicate, please re-checked input");
                }

                // save schedule
                driverScheduleService.saveDriverSchedule(lstDriverSchedule, lstDriverSchedulePath);
                msgWrapper = new MsgWrapper(Const.WS.OK, "");
            } catch (Exception ex) {
                if (ex instanceof LogicException) {
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

    @PostMapping("/api/addListDriverSchedule")
    @LogsActivityAnnotation
    @ApiOperation("function to list driver schedule")
    public MsgWrapper addListDriverSchedule(@ApiParam(value = "lstDriverSchedule", required = true) @RequestParam("lstDriverSchedule") List<DriverSchedule> lstDriverSchedule,
                                             @ApiParam(value = "driverSchedulePathGroupId", required = true) @RequestParam("driverSchedulePathGroupId") String driverSchedulePathGroupId
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(lstDriverSchedule) && !lstDriverSchedule.isEmpty()) {
            try {
                // validate schedule
                Long driverId = null, serviceId = null, fromHour = null, fromMinute = null, toHour = null, toMinute = null;
                List<Date> lstScheduleDate = new ArrayList<>();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                for (DriverSchedule driverSchedule : lstDriverSchedule){
                    if (Objects.isNull(driverId)){
                        driverId = driverSchedule.getDriverId();
                    }
                    if (Objects.isNull(serviceId)){
                        serviceId = driverSchedule.getServiceId();
                    }
                    if (Objects.isNull(fromHour)){
                        fromHour = driverSchedule.getFromHour();
                    }
                    if (Objects.isNull(fromMinute)){
                        fromMinute = driverSchedule.getFromMinute();
                    }
                    if (Objects.isNull(toHour)){
                        toHour = driverSchedule.getToHour();
                    }
                    if (Objects.isNull(toMinute)){
                        toMinute = driverSchedule.getToMinute();
                    }
                    try {
                        lstScheduleDate.add(simpleDateFormat.parse(driverSchedule.getStrScheduleDate()));
                    } catch (Exception ex) {}
                }

                MsgWrapper<com.an.common.bean.UserService> msg = userService.findUserService(driverId, serviceId);
                if (Objects.isNull(msg) || !Objects.equals(Const.WS.OK, msg.getCode()) || Objects.isNull(msg.getWrapper())){
                    return new MsgWrapper(Const.WS.NOK, "the driver doesn't register service: " + serviceId);
                }

                List<DriverSchedule> lst = driverScheduleService.findByDriverIdAndServiceIdAndListScheduleDateAndTime(driverId, serviceId, lstScheduleDate, fromHour, fromMinute, toHour, toMinute);
                if (lst != null && !lst.isEmpty()){
                    return new MsgWrapper(Const.WS.NOK, "the driver schedule has been duplicate, please re-checked input");
                }

                // save schedule
                driverScheduleService.addDriverSchedule(driverSchedulePathGroupId, lstDriverSchedule);
                msgWrapper = new MsgWrapper(Const.WS.OK, "");
            } catch (Exception ex) {
                if (ex instanceof LogicException) {
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

    @PostMapping("/api/cancelListDriverSchedule")
    @LogsActivityAnnotation
    @ApiOperation("function to cancel driver schedule")
    public MsgWrapper cancelListDriverSchedule(@ApiParam(value = "lstDriverScheduleId", required = true) @RequestParam("lstDriverScheduleId") List<Long> lstDriverScheduleId) throws Exception {
        MsgWrapper msgWrapper;
        try {
            driverScheduleService.cancelDriverSchedule(lstDriverScheduleId);
            msgWrapper = new MsgWrapper(Const.WS.OK, "");
        } catch (Exception e){
            if (e instanceof LogicException){
                msgWrapper = new MsgWrapper(Const.WS.NOK, e.getMessage());
            } else {
                throw e;
            }
        }
        return msgWrapper;
    }

    @PostMapping("/api/updateDriverSchedule")
    @LogsActivityAnnotation
    @ApiOperation("function to update driver schedule")
    public MsgWrapper updateDriverSchedule(@ApiParam(value = "driverScheduleId", required = true) @RequestParam("driverScheduleId") Long driverScheduleId,
                                           @ApiParam(value = "strScheduleDate") @RequestParam(value = "strScheduleDate", required = false) String strScheduleDate,
                                           @ApiParam(value = "fromHour") @RequestParam(value = "fromHour", required = false) Long fromHour,
                                           @ApiParam(value = "fromMinute") @RequestParam(value = "fromMinute", required = false) Long fromMinute,
                                           @ApiParam(value = "toHour") @RequestParam(value = "toHour", required = false) Long toHour,
                                           @ApiParam(value = "toMinute") @RequestParam(value = "toMinute", required = false) Long toMinute,
                                           @ApiParam(value = "status") @RequestParam(value = "status", required = false) String status
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(driverScheduleId)) {
            try {
                driverScheduleService.updateDriverSchedule(driverScheduleId, strScheduleDate, fromHour, fromMinute, toHour, toMinute, status);
                msgWrapper = new MsgWrapper(Const.WS.OK, "");
            } catch (Exception ex) {
                if (ex instanceof LogicException) {
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

    @GetMapping("/api/getDriverByFromPointAndScheduleDate")
    @LogsActivityAnnotation
    @ApiOperation("function to find by driver")
    public MsgWrapper getDriverByFromPointAndScheduleDate(@ApiParam(value = "serviceId", required = true) @RequestParam("serviceId") Long serviceId,
                                                                  @ApiParam(value = "x - latitude", required = true) @RequestParam("x") Double x,
                                                                  @ApiParam(value = "y - longitude", required = true) @RequestParam("y") Double y,
//                                                                  @ApiParam(value = "From schedule date. Format: ddMMyyyy", required = false) @RequestParam(value = "fromScheduleDate", required = false) String fromScheduleDate,
//                                                                  @ApiParam(value = "To schedule date. Format: ddMMyyyy", required = false) @RequestParam(value = "toScheduleDate", required = false) String toScheduleDate,
                                                                  @ApiParam(value = "List  schedule date. Format: ddMMyyyy", required = true) @RequestParam(value = "lstScheduleDate", required = true) List<String> lstScheduleDate,
                                                                  @ApiParam(value = "catch hour", required = false) @RequestParam(value = "fromHour", required = false) Long catchHour,
                                                                  @ApiParam(value = "catch minute", required = false) @RequestParam(value = "fromMinute", required = false) Long catchMinute,
//                                                                  @ApiParam(value = "to hour", required = false) @RequestParam(value = "toHour", required = false) Long toHour,
//                                                                  @ApiParam(value = "to minute", required = false) @RequestParam(value = "toMinute", required = false) Long toMinute,
                                                                  @ApiParam(value = "page", required = true) @RequestParam(value = "page", required = true) int page,
                                                                  @ApiParam(value = "size", required = true) @RequestParam(value = "size", required = true) int size
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(serviceId) && Objects.nonNull(x) && Objects.nonNull(y) && Objects.nonNull(lstScheduleDate) && !lstScheduleDate.isEmpty()) {
            try {
                Date fromSchedule = null, toSchedule = null;
                List<Date> lstSchedule = new ArrayList<>();
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    for (String schedule : lstScheduleDate) {
                        try {
                            Date scheduleDate = simpleDateFormat.parse(schedule);
                            lstSchedule.add(scheduleDate);
                            if (Objects.isNull(fromSchedule)){
                                fromSchedule = scheduleDate;
                            } else {
                                if (fromSchedule.after(scheduleDate)){
                                    fromSchedule = scheduleDate;
                                }
                            }
                            if (Objects.isNull(toSchedule)){
                                toSchedule = scheduleDate;
                            } else {
                                if (toSchedule.before(scheduleDate)){
                                    toSchedule = scheduleDate;
                                }
                            }
                        } catch (ParseException e) {
                            throw e;
                        }
                    }
                } catch (Exception ex) {
                    return new MsgWrapper(Const.WS.NOK, "invalid schedule date format");
                }
                List<DriverSchedule> lst = driverScheduleService.getDriverScheduleByFromPointAndScheduleDate(serviceId, x, y, fromSchedule, toSchedule, lstSchedule, catchHour, catchMinute, page, size);
                Map<Long, List<DriverSchedule>> map = new HashMap<>();
                if (lst != null && !lst.isEmpty()){
                    lst.forEach(schedule->{
                        if (map.containsKey(schedule.getDriverId())){
                            map.get(schedule.getDriverId()).add(schedule);
                        } else {
                            List<DriverSchedule> lstDriverSchedules = new ArrayList<>();
                            lstDriverSchedules.add(schedule);
                            map.put(schedule.getDriverId(), lstDriverSchedules);
                        }
                    });
                }
                List<User> lstUser = new ArrayList<>();
                // filter driver off service
                Set<Long> lstOffDriver = new HashSet<>();
                MsgWrapper<Service> msg = catalogService.getServiceById(serviceId);
                if (Objects.nonNull(msg) && Objects.nonNull(msg.getWrapper())){
                    DriverOff driverOff = driverOffService.findById(msg.getWrapper().getServiceGroupId());
                    if (Objects.nonNull(driverOff)){
                        lstOffDriver = driverOff.getLstDriver();
                    }
                }
                if (map != null && !map.isEmpty()){
                    Set<Long> finalLstOffDriver = lstOffDriver;
                    map.entrySet().stream().filter(schedule -> !finalLstOffDriver.contains(schedule.getKey())).forEach(schedule -> {
                        try {
                            MsgWrapper<User> msgWra = userService.getUserById(schedule.getKey());
                            if (Objects.nonNull(msgWra) && Objects.nonNull(msgWra.getWrapper())) {
                                User user = msgWra.getWrapper();
                                List<com.an.common.bean.UserService> lsUserServices = new ArrayList<>();
                                if (user.getLstUserService() != null && !user.getLstUserService().isEmpty()) {
                                    Optional<com.an.common.bean.UserService> optional = user.getLstUserService().stream().filter(s -> Objects.equals(s.getServiceId(), serviceId) && Objects.equals(s.getStatus(), Const.USER_SERVICE.STATUS_APPROVED)).findFirst();
                                    if (Objects.nonNull(optional) && optional.isPresent()) {
                                        lsUserServices.add(optional.get());
                                    }
                                }
                                if (!lsUserServices.isEmpty()) {
                                    user.setLstUserService(lsUserServices);
                                    user.setLstDriverSchedule(schedule.getValue());
                                    lstUser.add(user);
                                }
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    });
                }
                msgWrapper = new MsgWrapper(lstUser, Const.WS.OK, "");
            } catch (Exception ex) {
                if (ex instanceof LogicException) {
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

    @GetMapping("/api/getDriverScheduleByDriverId")
    @LogsActivityAnnotation
    @ApiOperation("function to find by driver schedule")
    public MsgWrapper getDriverScheduleByDriverId(@ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId,
                                                  @ApiParam(value = "serviceId", required = true) @RequestParam("serviceId") Long serviceId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(driverId)) {
            try {
                List<DriverSchedule> lst = driverScheduleService.findByDriverIdAndSchedule(driverId, serviceId, new Date());
                if (Objects.nonNull(lst) && !lst.isEmpty()){
                    lst.forEach(x->{
                        List<Booking> lstBooking = bookingService.findByDriverScheduleId(x.getDriverScheduleId());
                        x.setNumOfCust(Objects.nonNull(lstBooking) ? lstBooking.size() : 0);
                    });
                }
                msgWrapper = new MsgWrapper(lst, Const.WS.OK, "");
            } catch (Exception ex) {
                if (ex instanceof LogicException) {
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

    @GetMapping("/api/getDriverScheduleByDriverIdAndServiceIdAndStartDatetime")
    @LogsActivityAnnotation
    @ApiOperation("function to get booking by driver, service and start day")
    public MsgWrapper getDriverScheduleByDriverIdAndServiceIdAndStartDatetime(@ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId,
                                                                       @ApiParam(value = "serviceId", required = true) @RequestParam("serviceId") Long serviceId,
                                                                       @ApiParam(value = "From date. Format: ddMMyyyy", required = true) @RequestParam("fromDate") String fromDate,
                                                                       @ApiParam(value = "To date. Format: ddMMyyyy", required = true) @RequestParam("toDate") String toDate
    ) throws Exception {
        Date fromSchedule, toSchedule = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            fromSchedule = simpleDateFormat.parse(fromDate);
            if (!StringUtils.isEmpty(toDate)) {
                toSchedule = simpleDateFormat.parse(toDate);
            }
        } catch (Exception ex) {
            return new MsgWrapper(Const.WS.NOK, "invalid schedule date format");
        }
        List<DriverSchedule> lst = driverScheduleService.findByDriverIdAndServiceIdAndScheduleDate(driverId, serviceId, fromSchedule, toSchedule);
        Map<String, List<DriverSchedule>> map = new HashMap<>();
        String pathId = "";
        if (lst != null && !lst.isEmpty()){
            for (DriverSchedule x : lst) {
                if (StringUtils.isEmpty(pathId)){
                    pathId = x.getDriverSchedulePathGroupId();
                }
                if (map.containsKey(x.getDriverSchedulePathGroupId())){
                    map.get(x.getDriverSchedulePathGroupId()).add(x);
                } else {
                    List<DriverSchedule> lstDriverSchedules = new ArrayList<>();
                    lstDriverSchedules.add(x);
                    map.put(x.getDriverSchedulePathGroupId(), lstDriverSchedules);
                }
            }
        }
        List<ShareBean> lstShareBean = new ArrayList<>();
        map.entrySet().forEach(x->{
            ShareBean shareBean = new ShareBean();
            shareBean.setLstDriverSchedule(x.getValue());
            shareBean.setLstDriverSchedulePath(driverSchedulePathService.findByDriverSchedulePathGroupIdAndStatus(x.getKey(), Const.BOOKING_PATH.STATUS_ON));
            lstShareBean.add(shareBean);
        });
        return new MsgWrapper(lstShareBean, Const.WS.OK, "");
    }

    @GetMapping("/api/getBookingByDriverIdAndServiceIdAndStartDatetime")
    @LogsActivityAnnotation
    @ApiOperation("function to get booking by driver, service and start day")
    public MsgWrapper getBookingByDriverIdAndServiceIdAndStartDatetime(@ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId,
                                                                       @ApiParam(value = "serviceId", required = true) @RequestParam("serviceId") Long serviceId,
                                                                       @ApiParam(value = "From date. Format: ddMMyyyy", required = true) @RequestParam("fromDate") String fromDate,
                                                                       @ApiParam(value = "To date. Format: ddMMyyyy", required = true) @RequestParam("toDate") String toDate
    ) throws Exception {
        Date fromSchedule, toSchedule;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            fromSchedule = simpleDateFormat.parse(fromDate);
            toSchedule = simpleDateFormat.parse(toDate);
        } catch (Exception ex) {
            return new MsgWrapper(Const.WS.NOK, "invalid schedule date format");
        }
        List<Booking> lst = bookingService.getBookingByDriverIdAndServiceIdAndStartDatetime(driverId, serviceId, fromSchedule, toSchedule);
        Map<Long, List<Booking>> map = new HashMap<>();
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->{
                if (map.containsKey(x.getCustId())){
                    x.setCustMobile(map.get(x.getCustId()).get(0).getCustMobile());
                    x.setCustName(map.get(x.getCustId()).get(0).getCustName());
                    map.get(x.getCustId()).add(x);
                } else {
                    try {
                        MsgWrapper<User> msgWrapper = userService.getUserById(x.getCustId());
                        if (Objects.nonNull(msgWrapper) && Objects.nonNull(msgWrapper.getWrapper())){
                            x.setCustMobile(msgWrapper.getWrapper().getMobile());
                            x.setCustName(msgWrapper.getWrapper().getFullname());
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                    List<Booking> lstBooking = new ArrayList<>();
                    lstBooking.add(x);
                    map.put(x.getCustId(), lstBooking);
                }
            });
        }

        return new MsgWrapper(map.values(), Const.WS.OK, "");
    }

    @GetMapping("/api/getDriverScheduleById")
    @LogsActivityAnnotation
    @ApiOperation("function to get driver schedule by id")
    public MsgWrapper getDriverScheduleById(@ApiParam(value = "driverScheduleId", required = true) @RequestParam("driverScheduleId") Long driverScheduleId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(driverScheduleId)) {
            DriverSchedule driverSchedule = driverScheduleService.findById(driverScheduleId);
            if (Objects.nonNull(driverSchedule)) {
                driverSchedule.setLstDriverSchedulePath(driverSchedulePathService.findByDriverSchedulePathGroupIdAndStatus(driverSchedule.getDriverSchedulePathGroupId(), Const.BOOKING_PATH.STATUS_ON));
                Booking booking = bookingService.getBookingDetailByDriverScheduleId(driverScheduleId);
                if (Objects.nonNull(booking)){
                    driverSchedule.setNumOfCust(Objects.nonNull(booking.getBookingNum()) ? booking.getBookingNum().intValue() : 0);
                    driverSchedule.setMoneyTotal(booking.getMoneyTotal());
                }
                List<Booking> lstBooking = bookingService.findByDriverScheduleId(driverScheduleId);
                if (lstBooking != null && !lstBooking.isEmpty()){
                    lstBooking.forEach(x->{
                        try {
                            MsgWrapper<User> msg = userService.getUserById(x.getCustId());
                            if (Objects.nonNull(msg) && Objects.nonNull(msg.getWrapper())){
                                x.setCustName(msg.getWrapper().getFullname());
                                x.setCustMobile(msg.getWrapper().getMobile());
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    });
                }
                driverSchedule.setLstBooking(lstBooking);
            }
            msgWrapper = new MsgWrapper(driverSchedule, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/updateShareBooking")
    @LogsActivityAnnotation
    @ApiOperation("function to update driver for share booking")
    public MsgWrapper updateShareBooking (@ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId,
                                          @ApiParam(value = "lstAcceptedBooking", required = false) @RequestParam(value = "lstAcceptedBooking", required = false) List<Long> lstAcceptedBooking,
                                          @ApiParam(value = "lstRejectedBooking", required = false) @RequestParam(value = "lstRejectedBooking", required = false) List<Long> lstRejectedBooking
                                          ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(driverId)){
            try {
                bookingService.updateShareBooking(driverId, lstAcceptedBooking, lstRejectedBooking);
                msgWrapper = new MsgWrapper(Const.WS.OK, "");
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

    @GetMapping("/api/getShareBookingByCustId")
    @LogsActivityAnnotation
    @ApiOperation("function to get booking by cust id, and start datetime is greater than sysdate")
    public MsgWrapper getShareBookingByCustId(@ApiParam(value = "custId", required = true) @RequestParam("custId") Long custId) throws Exception {
        List<Booking> lst = bookingService.findShareBookingByCustId(custId);
        Map<String, List<Booking>> map = new HashMap<>();
        List<ShareBookingBean> lstShareBookingBean = new ArrayList<>();
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->{
                String key = x.getDriverId()+":"+x.getServiceId();
                if (!Objects.equals(x.getServiceId(), Const.SERVICE.SHARE_BIKE)){
                    List<Booking> lstBooking = bookingService.findByDriverScheduleId(x.getDriverScheduleId());
                    if (lstBooking != null && !lstBooking.isEmpty()) {
                        List<User> lstUser = new ArrayList<>();
                        lstBooking.stream().filter(z->(Objects.equals(z.getStatus(), Const.BOOKING.STATUS_ONGOING)
                                || Objects.equals(z.getStatus(), Const.BOOKING.STATUS_FINISH)
                                || Objects.equals(z.getStatus(), Const.BOOKING.STATUS_QUEUE))
                                && !Objects.equals(z.getCustId(), x.getCustId())
                        ).forEach(z->{
                                    try {
                                        MsgWrapper<User> msgWrapper = userService.getUserById(z.getCustId());
                                        if (Objects.nonNull(msgWrapper) && Objects.nonNull(msgWrapper.getWrapper())){
                                            User us = new User();
                                            us.setUserId(msgWrapper.getWrapper().getUserId());
                                            us.setFullname(msgWrapper.getWrapper().getFullname());
                                            lstUser.add(us);
                                        }
                                    } catch (Exception e) {
                                        logger.error(e.getMessage(), e);
                                    }
                                });
                        x.setLstUser(lstUser);
                    }
                }
                if (map.containsKey(key)){
                    map.get(key).add(x);
                } else {
                    List<Booking> lstBooking = new ArrayList<>();
                    lstBooking.add(x);
                    map.put(key, lstBooking);
                }
            });
        }
        map.entrySet().forEach(x->{
            ShareBookingBean bean = new ShareBookingBean();
            bean.setLstBooking(x.getValue());
            String key = x.getKey();
            String[] temp = key.split(":");
            bean.setDriverId(Long.valueOf(temp[0]));
            bean.setServiceId(Long.valueOf(temp[1]));
            try {
                MsgWrapper<User> msgWrapper = userService.getUserById(bean.getDriverId());
                if (Objects.nonNull(msgWrapper) && Objects.nonNull(msgWrapper.getWrapper())){
                    User user = msgWrapper.getWrapper();
                    bean.setDriverName(user.getFullname());
                    bean.setDriverMobile(user.getMobile());
                    bean.setDriverRating(user.getRating());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
        return new MsgWrapper(lstShareBookingBean, Const.WS.OK, "");
    }
}
