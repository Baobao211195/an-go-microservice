package com.an.booking.service;

import com.an.booking.client.CatalogService;
import com.an.booking.client.UserService;
import com.an.booking.entity.BookingEntity;
import com.an.booking.fcm.FCMService;
import com.an.booking.repository.BookingRepository;
import com.an.common.bean.*;
import com.an.common.exception.LogicException;
import com.an.common.utils.Const;
import com.google.gson.Gson;
import net.logstash.logback.argument.StructuredArguments;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sun.rmi.runtime.Log;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {

    private static Logger logger = LoggerFactory.getLogger(BookingService.class);
    private static Gson gson = new Gson();

    @Autowired
    BookingRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BookingDetailService bookingDetailService;

    @Autowired
    BookingCancelService bookingCancelService;

    @Autowired
    KafkaTemplate<String, Booking> kafkaTemplate;

    @Autowired
    DriverCancelService driverCancelService;

    @Autowired
    UserService userService;

    @Autowired
    FCMService fcmService;

    @Autowired
    DriverBusyService driverBusyService;

    @Autowired
    DriverScheduleService driverScheduleService;

    @Autowired
    CatalogService catalogService;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Booking findById(Long bookingId) {
        Optional<BookingEntity> optional = repository.findById(bookingId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            return modelMapper.map(optional.get(), Booking.class);
        }
        return null;
    }

    @Override
    public List<Booking> getOngoingByDriver(Long driverId) {
        List<Booking> output = new ArrayList<>();
        List<BookingEntity> lst = repository.findByDriverIdAndStatus(driverId, Const.BOOKING.STATUS_ONGOING);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x, Booking.class)));
        }
        return output;
    }

    @Override
    public List<Booking> getOngoingByCustomer(Long custId) {
        List<Booking> output = new ArrayList<>();
        List<BookingEntity> lst = repository.findByCustIdAndStatus(custId, Const.BOOKING.STATUS_ONGOING);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x, Booking.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking saveBooking(Booking booking) {
        BookingEntity entity = modelMapper.map(booking, BookingEntity.class);
        entity.setBookingDatetime(repository.getSysdate());
        entity.setStatus(Const.BOOKING.STATUS_QUEUE);
        entity.setPaymentStatus(Const.BOOKING.PAYMENT_STATUS_OFF);

        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, Booking.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking confirmPayment(Long bookingId, Long streetFee) {
        BookingEntity entity = repository.getOne(bookingId);
        if (Objects.nonNull(entity)){
            entity.setStreetFee(streetFee);
            entity.setPaymentStatus(Const.BOOKING.PAYMENT_STATUS_ON);

            entity = repository.save(entity);
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, Booking.class);
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Booking booking(Booking booking) throws LogicException, Exception {
        if (!Objects.equals(Const.SERVICE.SHARE_BIKE, booking.getServiceId())
                && !Objects.equals(Const.SERVICE.SHARE_CAR, booking.getServiceId())
                && !Objects.equals(Const.SERVICE.SHARE_CAR7, booking.getServiceId())) {
            // save booking
            Booking bean = saveBooking(booking);
            if (Objects.nonNull(bean)) {
                // save booking detail if have
                List<BookingDetail> lstBookingDetails = booking.getLstBookingDetail();
                if (lstBookingDetails != null && !lstBookingDetails.isEmpty()) {
                    List<BookingDetail> lst = bookingDetailService.createListBookingDetail(bean.getBookingId(), lstBookingDetails);
                }

                // send booking to kafka
                kafkaTemplate.send("booking", bean);

                return bean;
            }
        } else {
            if (booking.getLstDriverSchedule() != null && !booking.getLstDriverSchedule().isEmpty()){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                booking.getLstDriverSchedule().forEach(x->{
                    DriverSchedule driverSchedule = driverScheduleService.findById(x.getDriverScheduleId());
                    if (Objects.nonNull(driverSchedule)){
                        booking.setStartDatetime(driverSchedule.getScheduleDate());
                        booking.setDriverScheduleId(x.getDriverScheduleId());
                        booking.setDriverId(driverSchedule.getDriverId());
                        Booking bean = saveBooking(booking);

                        x.setStrScheduleDate(simpleDateFormat.format(driverSchedule.getScheduleDate()));
                        x.setBookingId(bean.getBookingId());
                    } else {
                        throw new LogicException(Const.WS.NOK, "invalid driver schedule");
                    }
                });
            }

            // send booking to kafka
            booking.setDriverScheduleId(null);
            booking.setStartDatetime(null);
            kafkaTemplate.send("booking", booking);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking updateDriver(Long bookingId, Long driverId) throws Exception, LogicException {
        MsgWrapper<User> msgWrapper = userService.getUserById(driverId);
        if (Objects.isNull(msgWrapper) || Objects.isNull(msgWrapper.getWrapper())){
            throw new LogicException(Const.WS.NOK, "invalid driver");
        } else {
            Optional<BookingEntity> optional = repository.findById(bookingId);
            if (Objects.nonNull(optional) && optional.isPresent()) {
                BookingEntity entity = optional.get();
                MsgWrapper<com.an.common.bean.UserService> msg = userService.findUserService(driverId, entity.getServiceId());
                if (Objects.nonNull(msg) && Objects.nonNull(msg.getWrapper())){
                    // update booking
                    entity.setDriverId(driverId);
                    entity.setStatus(Const.BOOKING.STATUS_ONGOING);
                    entity = repository.save(entity);

                    // remove driver cancel
                    driverCancelService.removeDriverCancel(bookingId);

                    // add driver to busy list
                    driverBusyService.insertDriver(entity.getServiceId(), entity.getDriverId());

                    // push fcm to customer
                    User user = msgWrapper.getWrapper();
                    Booking booking = modelMapper.map(entity, Booking.class);
                    FcmBean fcmBean = FcmBean.createFcmBean(booking);
                    fcmBean.setDriverId(user.getUserId());
                    fcmBean.setDriverMobile(user.getMobile());
                    fcmBean.setDriverName(user.getFullname());
                    fcmBean.setDriverRating(user.getRating());
                    fcmBean.setVehicleNo(msg.getWrapper().getVehicleNo());
                    fcmBean.setVehicleType(msg.getWrapper().getVehicleType());

                    // get driver location
                    try {
                        MsgWrapper<com.an.common.bean.Service> msgWrapp = catalogService.getServiceById(entity.getServiceId());
                        if (Objects.nonNull(msgWrapp) && Objects.nonNull(msgWrapp.getWrapper())){
                            MsgWrapper<UserLocation> msgWrapper1 = userService.getDriverLocation(entity.getDriverId(), msgWrapp.getWrapper().getServiceGroupId());
                            if (Objects.nonNull(msgWrapper1) && Objects.nonNull(msgWrapper1.getWrapper())){
                                fcmBean.setDriverX(msgWrapper1.getWrapper().getX());
                                fcmBean.setDriverY(msgWrapper1.getWrapper().getY());
                            }
                        }
                    } catch (Exception ex){
                        logger.error(ex.getMessage(), ex);
                    }

                    Map<String, String> map = new HashMap<>();
                    map.put("action", "2");
                    map.put("data", gson.toJson(fcmBean));
                    fcmService.sendMessageToToken(new PushNotificationRequest("Catch driver", "", "booking", entity.getCustToken(), map)); // gson.toJson(fcmBean)
                } else {
                    throw new LogicException(Const.WS.NOK, "invalid driver service");
                }
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelBooking(Long bookingId, Long cancelUserId, String userType, String reason) throws LogicException, Exception {
        // update booking
        Optional<BookingEntity> optional = repository.findById(bookingId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            BookingEntity entity = optional.get();
            if (Objects.equals(Const.USER.TYPE_DRIVER, userType)) {
                if (!Objects.equals(Const.BOOKING.STATUS_ONGOING, entity.getStatus())) {
                    throw new LogicException(Const.WS.NOK, "the booking is invalid status");
                }
            } else {
                if (!Objects.equals(Const.BOOKING.STATUS_ONGOING, entity.getStatus()) && !Objects.equals(Const.BOOKING.STATUS_QUEUE, entity.getStatus())) {
                    throw new LogicException(Const.WS.NOK, "the booking is invalid status");
                }
            }
            entity.setStatus(Const.BOOKING.STATUS_CANCEL);
            repository.save(entity);

            // remove driver to busy list
            driverBusyService.evictDriver(entity.getServiceId(), entity.getDriverId());

            // push notification for customer or driver
            String token = "";
            Long userId = null;
            if (Objects.equals(Const.USER.TYPE_CUSTOMER, userType)){
                userId = entity.getDriverId();
            } else {
                userId = entity.getCustId();
            }
            if (Objects.nonNull(userId)) {
                MsgWrapper<User> msgWrapper = userService.getUserById(userId);
                if (Objects.nonNull(msgWrapper) && Objects.nonNull(msgWrapper.getWrapper())) {
                    token = msgWrapper.getWrapper().getFcmToken();
                }
            }
            if (!StringUtils.isEmpty(token)) {
                Map<String, String> map = new HashMap<>();
                map.put("action", "5");
                map.put("data", gson.toJson(entity));
                if (Objects.equals(Const.USER.TYPE_CUSTOMER, userType)){
                    map.put("message", "Khách hàng đã huỷ đặt chuyến " + bookingId + ", dịch vụ: " + Const.SERVICE.convertIdToName(entity.getServiceId()) + ", khởi hành ngày: " +entity.getStartDatetime());
                } else {
                    map.put("message", "Tài xế đã huỷ đặt chuyến " + bookingId + ", dịch vụ:  " + Const.SERVICE.convertIdToName(entity.getServiceId()) + ", khởi hành ngày: " +entity.getStartDatetime()+  ". Quý khách vui lòng kiểm tra lại để đảm bảo lịch trình của mình");
                }
                fcmService.sendMessageToToken(new PushNotificationRequest("Cancel booking", "", "cancelBooking", token, map)); // gson.toJson(entity)
            } else {
                throw new LogicException(Const.WS.NOK, "invalid cancel user");
            }
        }

        // save booking cancel
        bookingCancelService.saveBookingCancel(bookingId, cancelUserId, userType, reason, repository.getSysdate());

        // remove driver cancel
        driverCancelService.removeDriverCancel(bookingId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishBooking(Long bookingId) throws Exception, LogicException {
        // update booking
        Optional<BookingEntity> optional = repository.findById(bookingId);
        if (Objects.nonNull(optional) && optional.isPresent()) {
            BookingEntity entity = optional.get();
            entity.setStatus(Const.BOOKING.STATUS_FINISH);
            entity.setEndDatetime(repository.getSysdate());
            repository.save(entity);

            // update user wallet
            if (Objects.nonNull(entity.getUserWalletId()) && entity.getUserWalletId() > 0L && Objects.nonNull(entity.getTotalFee()) && entity.getTotalFee() > 0L) {
                try {
                    // update minus balance
                    MsgWrapper<UserWallet> msgWrapper = userService.updateUserWallet(entity.getUserWalletId(), Const.USER_WALLET.OPERATE_MINUS, Double.valueOf(entity.getTotalFee()));
                    if (Objects.isNull(msgWrapper) || Objects.isNull(msgWrapper.getWrapper())) {
                        throw new LogicException(Const.WS.NOK, "fail to minus balance");
                    }

                    // update reduce user promotion times
                    MsgWrapper<UserPromotion> msgWrap = catalogService.reduceUserPromotionTimes(entity.getUserPromotionId());
                    if (Objects.isNull(msgWrap) || Objects.isNull(msgWrap.getWrapper())) {
                        throw new LogicException(Const.WS.NOK, "fail to reduce user promotion times");
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    throw ex;
                }
            }

            // update user wallet distance
            MsgWrapper<UserWallet> msgWrapperDistance = userService.getUserWalletByType(entity.getCustId(), Const.USER_WALLET.BALANCE_TYPE_DISTANCE);
            if (Objects.nonNull(msgWrapperDistance) && Objects.nonNull(msgWrapperDistance.getWrapper())){
                userService.updateUserWallet(msgWrapperDistance.getWrapper().getUserWalletId(), Const.USER_WALLET.OPERATE_ADDED, entity.getDistance());
            }
            msgWrapperDistance = userService.getUserWalletByType(entity.getDriverId(), Const.USER_WALLET.BALANCE_TYPE_DISTANCE);
            if (Objects.nonNull(msgWrapperDistance) && Objects.nonNull(msgWrapperDistance.getWrapper())){
                userService.updateUserWallet(msgWrapperDistance.getWrapper().getUserWalletId(), Const.USER_WALLET.OPERATE_ADDED, entity.getDistance());
            }

            // remove driver to busy list
            driverBusyService.evictDriver(entity.getServiceId(), entity.getDriverId());

            // push notification for customer
            String token = "";
            MsgWrapper<User> msgWrapper = userService.getUserById(entity.getCustId());
            if (Objects.nonNull(msgWrapper) && Objects.nonNull(msgWrapper.getWrapper())){
                token = msgWrapper.getWrapper().getFcmToken();
            }
            if (!StringUtils.isEmpty(token)) {
                Booking booking = modelMapper.map(entity, Booking.class);
                FcmBean fcmBean = FcmBean.createFcmBean(booking);
                MsgWrapper<User> msgWrap = userService.getUserById(entity.getDriverId());
                if (Objects.nonNull(msgWrap) && Objects.nonNull(msgWrap.getWrapper())) {
                    User user = msgWrapper.getWrapper();
                    fcmBean.setDriverId(user.getUserId());
                    fcmBean.setDriverMobile(user.getMobile());
                    fcmBean.setDriverName(user.getFullname());
                    fcmBean.setDriverRating(user.getRating());
                    if (Objects.nonNull(user.getLstUserService()) && !user.getLstUserService().isEmpty()) {
                        user.getLstUserService().forEach(x->{
                            if (Objects.equals(x.getServiceId(), entity.getServiceId())){
                                fcmBean.setVehicleNo(x.getVehicleNo());
                                fcmBean.setVehicleType(x.getVehicleType());
                            }
                        });
                    }
                }

                Map<String, String> map = new HashMap<>();
                map.put("action", "4");
                map.put("data", gson.toJson(fcmBean));
                fcmService.sendMessageToToken(new PushNotificationRequest("Finished booking", "", "finishedBooking", token, map)); // gson.toJson(entity)
            } else {
                throw new LogicException(Const.WS.NOK, "invalid cancel user");
            }
        }

        // remove driver cancel
        driverCancelService.removeDriverCancel(bookingId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOngoingBooking(Long bookingId) throws Exception, LogicException {
        Booking booking = updateBookingStatus(bookingId, Const.BOOKING.STATUS_ONGOING);

        // insert busy driver list
        driverBusyService.insertDriver(booking.getServiceId(), booking.getDriverId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking updateBookingStatus(Long bookingId, String status) {
        Optional<BookingEntity> optional = repository.findById(bookingId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            BookingEntity entity = optional.get();
            entity.setStatus(status);
            entity = repository.save(entity);
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, Booking.class);
            }
        }
        return null;
    }

    @Override
    public List<Booking> getBookingReportByDay(Long driverId) {
        List<Booking> output = new ArrayList<>();
        List<BookingEntity> lst = repository.getBookingReportByDay(driverId);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x, Booking.class)));
        }
        return output;
    }

    @Override
    public Booking getBookingDetailByDriverScheduleId(Long driverScheduleId) {
        List<BookingEntity> lst = repository.getBookingDetailByDriverScheduleId(driverScheduleId);
        if (lst != null && !lst.isEmpty()){
            return modelMapper.map(lst.get(0), Booking.class);
        }
        return null;
    }

    @Override
    public List<Booking> getUnfinishBookingByDriverIdAndServiceIdOnNowDate(Long driverId, Long serviceId) {
        List<Booking> output = new ArrayList<>();
        String sql = " select a from BookingEntity a where a.driverId = :driverId and a.serviceId = :serviceId and status = :status and a.startDatetime >= date(sysdate())";

        Query query = entityManager.createQuery(sql);
        query.setParameter("driverId", driverId);
        query.setParameter("serviceId", serviceId);
        query.setParameter("status", Const.BOOKING.STATUS_QUEUE);

        List<BookingEntity> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, Booking.class)));
        }
        return output;
    }

    @Override
    public List<Booking> getBookingByDriverIdAndServiceIdAndStartDatetime(Long driverId, Long serviceId, Date fromDate, Date toDate) {
        List<Booking> output = new ArrayList<>();
        String sql = " select a from BookingEntity a where a.driverId = :driverId and a.serviceId = :serviceId and a.startDatetime >= :fromDate and a.startDatetime <= :toDate order by a.custId, a.startDatetime";

        Query query = entityManager.createQuery(sql);
        query.setParameter("driverId", driverId);
        query.setParameter("serviceId", serviceId);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        List<BookingEntity> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, Booking.class)));
        }
        return output;
    }

    @Override
    @Cacheable(value = "findByDriverScheduleId", key = "#driverScheduleId", unless = "#result == null ")
    public List<Booking> findByDriverScheduleId(Long driverScheduleId) {
        List<Booking> output = new ArrayList<>();
        List<BookingEntity> lst = repository.findByDriverScheduleId(driverScheduleId);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, Booking.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShareBooking (Long driverId, List<Long> lstAcceptedBooking, List<Long> lstRejectedBooking) throws LogicException, Exception {
        MsgWrapper<User> msgWrapper = userService.getUserById(driverId);
        if (Objects.isNull(msgWrapper) || Objects.isNull(msgWrapper.getWrapper())){
            throw new LogicException(Const.WS.NOK, "invalid driver");
        } else {
            FcmBean fcmBean = null;
            if (lstAcceptedBooking != null && !lstAcceptedBooking.isEmpty()) {
                for (Long bookingId : lstAcceptedBooking) {
                    Optional<BookingEntity> optional = repository.findById(bookingId);
                    if (Objects.nonNull(optional) && optional.isPresent()) {
                        BookingEntity entity = optional.get();
                        MsgWrapper<com.an.common.bean.UserService> msg = userService.findUserService(driverId, entity.getServiceId());
                        if (Objects.nonNull(msg) && Objects.nonNull(msg.getWrapper())) {
                            // update booking
                            entity.setDriverId(driverId);
                            entity = repository.save(entity);

                            // push fcm to customer
                            if (Objects.isNull(fcmBean)) {
                                User user = msgWrapper.getWrapper();
                                Booking booking = modelMapper.map(entity, Booking.class);
                                fcmBean = FcmBean.createFcmBean(booking);
                                fcmBean.setDriverId(user.getUserId());
                                fcmBean.setDriverMobile(user.getMobile());
                                fcmBean.setDriverName(user.getFullname());
                                fcmBean.setDriverRating(user.getRating());
                                fcmBean.setVehicleNo(msg.getWrapper().getVehicleNo());
                                fcmBean.setVehicleType(msg.getWrapper().getVehicleType());
                            }
                        } else {
                            throw new LogicException(Const.WS.NOK, "invalid driver service");
                        }
                    }
                }
            }
            if (lstRejectedBooking != null && !lstRejectedBooking.isEmpty()) {
                for (Long bookingId : lstRejectedBooking) {
                    Optional<BookingEntity> optional = repository.findById(bookingId);
                    if (Objects.nonNull(optional) && optional.isPresent()) {
                        BookingEntity entity = optional.get();
                        // update booking
                        entity.setStatus(Const.BOOKING.STATUS_CANCEL);
                        repository.save(entity);

                        // push fcm to customer
                        if (Objects.isNull(fcmBean)) {
                            Booking booking = modelMapper.map(entity, Booking.class);
                            fcmBean = FcmBean.createFcmBean(booking);
                        }
                    }
                }
            }
            if (Objects.isNull(fcmBean)){
                throw new LogicException(Const.WS.NOK, "invalid booking id");
            }
            fcmBean.setLstAcceptedBooking(lstAcceptedBooking);
            fcmBean.setLstRejectedBooking(lstRejectedBooking);
            Map<String, String> map = new HashMap<>();
            map.put("action", "2");
            map.put("data", gson.toJson(fcmBean));
            String token = "";
            MsgWrapper<User> msg = userService.getUserById(fcmBean.getCustId());
            if (Objects.nonNull(msg) && Objects.nonNull(msg.getWrapper())){
                token = msg.getWrapper().getFcmToken();
            }
            fcmService.sendMessageToToken(new PushNotificationRequest("Catch driver", "", "booking", token, map)); // gson.toJson(fcmBean)
        }
    }

    @Override
    public List<Booking> findShareBookingByCustId(Long custId) {
        List<Booking> output = new ArrayList<>();
        List<BookingEntity> lst = repository.findShareBookingByCustId(custId);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x, Booking.class)));
        }
        return output;
    }

    @Override
    public List<Booking> findBookingHistoryByCustId(Long custId) {
        List<Booking> output = new ArrayList<>();
        List<BookingEntity> lst = repository.findBookingHistoryByCustId(custId);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x, Booking.class)));
        }
        return output;
    }

    @Override
    public List<Booking> findBookingHistoryByDriverId(Long driverId) {
        List<Booking> output = new ArrayList<>();
        List<BookingEntity> lst = repository.findBookingHistoryByDriverId(driverId);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x, Booking.class)));
        }
        return output;
    }
}
