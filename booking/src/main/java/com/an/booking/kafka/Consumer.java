package com.an.booking.kafka;

import com.an.booking.client.CatalogService;
import com.an.booking.client.UserService;
import com.an.booking.fcm.FCMService;
import com.an.booking.service.*;
import com.an.common.bean.*;
import com.an.common.utils.Const;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.util.*;

@Configuration
@EnableKafka
public class Consumer {

    private static Logger logger = LoggerFactory.getLogger(Consumer.class);
    private static Gson gson = new Gson();

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    FCMService fcmService;

    @Autowired
    UserService userService;

    @Autowired
    BookingService bookingService;

    @Autowired
    DriverCancelService driverCancelService;

    @Autowired
    DriverBusyService driverBusyService;

    @Autowired
    DriverOffService driverOffService;

    @Autowired
    DriverScheduleService driverScheduleService;

    @Autowired
    CatalogService catalogService;

    @Autowired
    KafkaTemplate<String, Booking> kafkaTemplate;

    @Bean
    public ConsumerFactory<String, Booking> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory(props, new StringDeserializer(), new JsonDeserializer<>(Booking.class));
//        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Booking> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Booking> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(1);
        return factory;
    }

    @KafkaListener(topics = "booking", groupId = "BookingKafka", properties = {})
    public void consumer(Booking booking) throws Exception {
        logger.info(booking.toString());
        UserLocation driver = getDriver(booking);
        if (Objects.nonNull(driver)) {
            try {
                FcmBean fcmBean = FcmBean.createFcmBean(booking);
                MsgWrapper<User> msgWrapper = userService.getUserById(driver.getUserId());
                String fcmToken = "";
                if (Objects.nonNull(msgWrapper) && Objects.nonNull(msgWrapper.getWrapper())) {
                    fcmBean.setCustMobile(msgWrapper.getWrapper().getMobile());
                    fcmBean.setDriverId(msgWrapper.getWrapper().getUserId());
                    fcmToken = msgWrapper.getWrapper().getFcmToken();
                }
                // push notification to driver
                Map<String, String> map = new HashMap<>();
                map.put("action", "1");
                map.put("data", gson.toJson(fcmBean));
                logger.info("fcmBean: "+  gson.toJson(fcmBean));
                logger.info("fcmToken: "+  fcmToken);
                fcmService.sendMessageToToken(new PushNotificationRequest("New booking", "", "booking", fcmToken, map)); // gson.toJson(fcmBean)
            } catch (Exception ex){
                logger.error(ex.getMessage(), ex);
                if (Objects.equals(booking.getRetry(), 1)){
                    bookingService.updateBookingStatus(booking.getBookingId(), Const.BOOKING.STATUS_CANCEL_SYSTEM_ERROR);
                } else {
                    booking.setRetry(1);
                    kafkaTemplate.send("booking", booking);
                }
            }
        } else {
            MsgWrapper<User> msgWrapper = userService.getUserById(booking.getCustId());
            String fcmToken = booking.getCustToken();
            if (Objects.nonNull(msgWrapper) && Objects.nonNull(msgWrapper.getWrapper())){
                fcmToken = msgWrapper.getWrapper().getFcmToken();
            }
            // update cancel booking because have not available driver
            bookingService.updateBookingStatus(booking.getBookingId(), Const.BOOKING.STATUS_CANCEL_NO_DRIVER);

            // push notification for customer
            Map<String, String> map = new HashMap<>();
            map.put("action", "1");
            map.put("data", gson.toJson(booking));
            fcmService.sendMessageToToken(new PushNotificationRequest("Non driver", "", "non-driver", fcmToken, map)); // gson.toJson(booking)
        }
    }

    private UserLocation getDriver (Booking booking){
        try {
            Long serviceGroupId = null;
            MsgWrapper<Service> msg = catalogService.getServiceById(booking.getServiceId());
            if (Objects.nonNull(msg) && Objects.nonNull(msg.getWrapper())){
                serviceGroupId = msg.getWrapper().getServiceGroupId();
            }
            if (Objects.equals(Const.SERVICE.TRIP, booking.getServiceId())){
                MsgWrapper<List<User>> msgWrapper = userService.getDriverByProvince(booking.getProvince());
                if (Objects.nonNull(msgWrapper) && Objects.nonNull(msgWrapper.getWrapper())
                        && !msgWrapper.getWrapper().isEmpty()){
                    List<User> lstDriver = msgWrapper.getWrapper();
                    Set<Long> set = new HashSet<>();
                    DriverCancel driverCancel = driverCancelService.getListDriverCancel(booking.getBookingId());
                    if (Objects.nonNull(driverCancel) && Objects.nonNull(driverCancel.getLstDriver())) {
                        set.addAll(driverCancel.getLstDriver());
                    }
                    if (Objects.nonNull(serviceGroupId)){
                        DriverOff driverOff = driverOffService.findById(serviceGroupId);
                        if (Objects.nonNull(driverOff) && Objects.nonNull(driverOff.getLstDriver())) {
                            set.addAll(driverOff.getLstDriver());
                        }
                    }
                    if (!set.isEmpty()) {
                        Optional<User> optional = lstDriver.stream().filter(z -> !set.contains(z.getUserId())).findFirst();
                        if (Objects.nonNull(optional) && optional.isPresent()) {
                            UserLocation userLocation = new UserLocation();
                            userLocation.setUserId(optional.get().getUserId());
                            return userLocation;
                        }
                    } else {
                        UserLocation userLocation = new UserLocation();
                        userLocation.setUserId(lstDriver.get(0).getUserId());
                        return userLocation;
                    }
                }
                return null;
            } else if (Objects.equals(Const.SERVICE.SHARE_BIKE, booking.getServiceId())
                    || Objects.equals(Const.SERVICE.SHARE_CAR, booking.getServiceId())
                    || Objects.equals(Const.SERVICE.SHARE_CAR7, booking.getServiceId())
            ) {
                MsgWrapper<User> msgWrapper = userService.getUserById(booking.getDriverId());
                if (Objects.nonNull(msgWrapper) && Objects.nonNull(msgWrapper.getWrapper())) {
                    UserLocation userLocation = new UserLocation();
                    userLocation.setUserId(msgWrapper.getWrapper().getUserId());
                    userLocation.setToken(msgWrapper.getWrapper().getFcmToken());
                    return userLocation;
                }
            } else {
                MsgWrapper<List<UserLocation>> msgWrapper = userService.getUserByLocation(serviceGroupId, booking.getFromX(), booking.getFromY());
                if (Objects.nonNull(msgWrapper) && Objects.nonNull(msgWrapper.getWrapper()) && !msgWrapper.getWrapper().isEmpty()) {
                    List<UserLocation> lstUserLocation = new ArrayList<>();
                    msgWrapper.getWrapper().forEach(x -> {
                        try {
                            MsgWrapper<com.an.common.bean.UserService> msgWrapp = userService.findUserService(x.getUserId(), booking.getServiceId());
                            if (Objects.nonNull(msgWrapp) && Objects.nonNull(msgWrapp.getWrapper())) {
                                lstUserLocation.add(x);
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    });
                    if (!lstUserLocation.isEmpty()) {
                        Set<Long> set = new HashSet<>();
                        DriverCancel driverCancel = driverCancelService.getListDriverCancel(booking.getBookingId());
                        if (Objects.nonNull(driverCancel) && Objects.nonNull(driverCancel.getLstDriver())) {
                            set.addAll(driverCancel.getLstDriver());
                        }
                        DriverBusy driverBusy = driverBusyService.findById(booking.getServiceId());
                        if (Objects.nonNull(driverBusy) && Objects.nonNull(driverBusy.getLstDriver())) {
                            set.addAll(driverBusy.getLstDriver());
                        }
                        DriverOff driverOff = driverOffService.findById(serviceGroupId);
                        if (Objects.nonNull(driverOff) && Objects.nonNull(driverOff.getLstDriver())) {
                            set.addAll(driverOff.getLstDriver());
                        }
                        if (!set.isEmpty()) {
                            Optional<UserLocation> optional = lstUserLocation.stream().filter(userLocation -> !set.contains(userLocation.getUserId())).findFirst();
                            if (Objects.nonNull(optional) && optional.isPresent()) {
                                return optional.get();
                            }
                        } else {
                            return msgWrapper.getWrapper().get(0);
                        }
                    }
                }
            }
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

}
