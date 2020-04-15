package com.an.user.service;

import com.an.common.bean.*;
import com.an.common.exception.LogicException;
import com.an.common.logger.LogsActivityAnnotation;
import com.an.common.swagger.SwaggerDocument;
import com.an.common.utils.Const;
import com.an.common.utils.MailUtils;
import com.an.common.utils.OtpUtils;
import com.an.user.client.AuthService;
import com.an.user.client.CatalogService;
import com.an.user.component.GoogleVerifyToken;
import com.an.user.config.AppConfig;
import com.an.user.entity.OtpEntity;
import com.an.user.entity.UserLocationEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SwaggerDocument(name = "USER_API")
@Api(value = "User management service", description = "Endpoints for handling user management flow")
public class ApiService {

    private static Logger logger = LoggerFactory.getLogger(ApiService.class);

    @Autowired
    AppConfig appConfig;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    UserServiceService userServiceService;

    @Autowired
    UserWalletService userWalletService;

    @Autowired
    UserServiceProfileService userServiceProfileService;

    @Autowired
    CommonService commonService;

    @Autowired
    OtpService otpService;

    @Autowired
    UserBikeLocationService userBikeLocationService;

    @Autowired
    UserCarLocationService userCarLocationService;

    @Autowired
    UserCar7LocationService userCar7LocationService;

    @Autowired
    UserTruckLocationService userTruckLocationService;

    @Autowired
    MailUtils mailUtils;

    @Autowired
    GoogleVerifyToken googleVerifyToken;

    @Autowired
    AuthService authService;

    @Autowired
    CatalogService catalogService;

    @GetMapping("/security/sendOtp")
    @LogsActivityAnnotation
    @ApiOperation("function to send otp")
    public MsgWrapper sendOtp (@ApiParam(value = "mobile", required = true) @RequestParam("mobile") String mobile) throws Exception {
        MsgWrapper msgWrapper;
        if (!StringUtils.isEmpty(mobile)){
            String otp = OtpUtils.sendOtp(appConfig.getSmsToken(), mobile);
            if (!StringUtils.isEmpty(otp)) {
                // put into redis
                otpService.createOtp(mobile, otp);

                // return otp to client
                msgWrapper = new MsgWrapper(otp, Const.WS.OK, "");
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "the system just could not send otp");
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/security/sendOtpOverEmail")
    @LogsActivityAnnotation
    @ApiOperation("function to send otp")
    public MsgWrapper sendOtpOverEmail (@ApiParam(value = "email", required = true) @RequestParam("email") String email) throws Exception {
        MsgWrapper msgWrapper;
        if (!StringUtils.isEmpty(email)){
            String otp = mailUtils.sendOtp(email);
            if (!StringUtils.isEmpty(otp)) {
                // put into redis
                otpService.createOtp(email, otp);

                // return otp to client
                msgWrapper = new MsgWrapper(otp, Const.WS.OK, "");
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "the system just could not send otp");
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/security/checkOtp")
    @LogsActivityAnnotation
    @ApiOperation("function to send otp")
    public MsgWrapper checkOtp (@ApiParam(value = "mobileOrEmail", required = true) @RequestParam("mobileOrEmail") String mobileOrEmail, @ApiParam(value = "otp", required = true) @RequestParam("otp") String otp) throws Exception {
        MsgWrapper msgWrapper;
        if (!StringUtils.isEmpty(mobileOrEmail) && !StringUtils.isEmpty(otp)){
            // get otp
            OtpEntity otpEntity = otpService.getOtpByIsdn(mobileOrEmail);
            if (Objects.nonNull(otpEntity) && Objects.equals(otpEntity.getOtp(), otp)) {
                msgWrapper = new MsgWrapper(Const.WS.OK, "");
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid otp");
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/security/findByUsername")
    @LogsActivityAnnotation
    @ApiOperation("function to get user by username")
    public MsgWrapper findByUsername(@ApiParam(value = "username", required = true) @RequestParam("username") String username) throws Exception {
        MsgWrapper msgWrapper;
        if (!StringUtils.isEmpty(username)) {
            String[] temp = username.split(Const.USER.USERNAME_CONNECT_CHAR, 2);
            User user;
            if (temp[0].startsWith(Const.USER.GOOGLE_USER_PREFIX)) {
                String param = temp[0].replace(Const.USER.GOOGLE_USER_PREFIX, "");
                User verify = googleVerifyToken.verify(param);
                if (Objects.isNull(verify)){
                    return new MsgWrapper(Const.WS.NOK, "social token invalid");
                } else {
                    user = userService.findBySocialIdAndEmailAndType(verify.getSocialId(), verify.getEmail(), temp[1]);
                }
            } else {
                user = userService.findByMobileAndTypeAndStatusIn(temp[0], temp[1], Arrays.asList(Const.USER.STATUS_ON));
            }
            if (Objects.nonNull(user)) {
                if (temp[0].startsWith(Const.USER.GOOGLE_USER_PREFIX)) {
                    user.setPassword(passwordEncoder.encode(temp[0].replace(Const.USER.GOOGLE_USER_PREFIX, "")));
                } else {
                    // get otp
                    OtpEntity otpEntity = otpService.getOtpByIsdn(user.getMobile());
                    if (Objects.nonNull(otpEntity)) {
                        user.setPassword(passwordEncoder.encode(otpEntity.getOtp()));
                    }
//                    user.setPassword(passwordEncoder.encode("8888"));
                }

                // return result
                msgWrapper = new MsgWrapper(Const.WS.OK, "");
                msgWrapper.setWrapper(user);
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid user");
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/api/getUserById")
    @LogsActivityAnnotation
    @ApiOperation("function to get user by user id")
    public MsgWrapper getUserById(@ApiParam(value = "userId", required = true) @RequestParam("userId") Long userId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userId)) {
            User user = userService.findById(userId);
            if (Objects.nonNull(user)){
                List<com.an.common.bean.UserService> lstUserService = userServiceService.findByUserId(userId);
                user.setLstUserService(lstUserService);
                if (lstUserService != null && !lstUserService.isEmpty()){
                    Set<Long> set = new HashSet<>();
                    lstUserService.forEach(x->{
                        try {
                            MsgWrapper<Service> msg = catalogService.getServiceById(x.getServiceId());
                            if (Objects.nonNull(msg) && Objects.nonNull(msg.getWrapper())){
                                set.add(msg.getWrapper().getServiceGroupId());
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    });
                    if (!set.isEmpty()){
                        user.setLstServiceGroupId(new ArrayList<>(set));
                    }
                }
                user.setLstUserWallet(userWalletService.findByUserId(userId));
                msgWrapper = new MsgWrapper(user, Const.WS.OK, "");
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid user");
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/security/createUser")
    @LogsActivityAnnotation
    @ApiOperation("function to create new user")
    public MsgWrapper createUser(@ApiParam(value = "fullname") @RequestParam(name = "fullname", required = false) String fullname,
                                 @ApiParam(value = "mobile", required = true) @RequestParam("mobile") String mobile,
                                 @ApiParam(value = "email") @RequestParam(value = "email", required = false) String email,
                                 @ApiParam(value = "type", required = true) @RequestParam("type") String type,
                                 @ApiParam(value = "socialToken") @RequestParam(name = "socialToken", required = false) String socialToken,
                                 @ApiParam(value = "otp", required = true) @RequestParam(name = "otp") String otp,
                                 @ApiParam(value = "fcmToken", required = true) @RequestParam(name = "fcmToken") String fcmToken,
                                 @ApiParam(value = "avatar") @RequestParam(name = "avatar", required = false) MultipartFile file
                                 ) throws Exception {
        MsgWrapper msgWrapper;
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(type) || StringUtils.isEmpty(fullname) ||  (StringUtils.isEmpty(email) && StringUtils.isEmpty(socialToken))) {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        } else {
            User checked = userService.findByMobileAndTypeAndStatusIn(mobile, Const.USER.TYPE_CUSTOMER, Arrays.asList(Const.USER.STATUS_ON, Const.USER.STATUS_UNAPPROVED));
            if (Objects.isNull(checked)){
                // save user
                String avatar = Objects.nonNull(file) ? file.getName() : "";
                String socialId = null;
                if (!StringUtils.isEmpty(socialToken)){
                    User verify = googleVerifyToken.verify(socialToken);
                    if (Objects.isNull(verify)){
                        return new MsgWrapper(Const.WS.NOK, "invalid social token");
                    } else {
                        email = verify.getEmail();
                        socialId = verify.getSocialId();
                    }
                }

                // check social id
                User socialUser = userService.findBySocialIdAndEmailAndType(socialId, email, Const.USER.TYPE_CUSTOMER);
                if (Objects.nonNull(socialUser)){
                    return new MsgWrapper(Const.WS.NOK, "the socialId has already existed");
                }

                User user = userService.createUser(fullname, mobile, email, avatar, type, socialId, fcmToken, "", "");
                if (Objects.nonNull(user)) {
                    String message = "successfully";
                    // store avatar
                    if (Objects.nonNull(file)) {
                        User update = userService.updateAvatarUser(user.getUserId(), file);
                        if (Objects.isNull(update)){
                            message += " but upload avatar fail";
                        }
                    }

                    // return result
                    msgWrapper = new MsgWrapper(Const.WS.OK, message);
                    if (Objects.nonNull(otp)){
                        String token = authService.autoSignIn("Basic YW5nbzphbmdvQDIwMTk=", mobile+"@0", otp, "password");
                        msgWrapper.setWrapper(token);
                    } else {
                        msgWrapper.setWrapper(user);
                    }
                } else {
                    msgWrapper = new MsgWrapper(Const.WS.NOK, "the system has error when create new user");
                }
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "the mobile has already existed");
            }
        }
        return msgWrapper;
    }

    @PostMapping("/security/createDriver")
    @LogsActivityAnnotation
    @ApiOperation("function to update user profile")
    public MsgWrapper createDriver(@ApiParam(value = "mobile", required = true) @RequestParam("mobile") String mobile,
                                        @ApiParam(value = "socialToken", required = true) @RequestParam("socialToken") String socialToken,
                                        @ApiParam(value = "fullname", required = true) @RequestParam(name = "fullname") String fullname,
                                        @ApiParam(value = "province", required = true) @RequestParam(name = "province") String province,
                                        @ApiParam(value = "serviceGroupId", required = true) @RequestParam(name = "serviceGroupId") Long serviceGroupId,
                                        @ApiParam(value = "productId") @RequestParam(name = "productId", required = false) Long productId,
                                        @ApiParam(value = "vehicleNo", required = true) @RequestParam(name = "vehicleNo") String vehicleNo,
                                        @ApiParam(value = "vehicleType", required = true) @RequestParam(name = "vehicleType") String vehicleType,
                                        @ApiParam(value = "fcmToken", required = true) @RequestParam(name = "fcmToken") String fcmToken,
                                        @ApiParam(value = "inviteCode", required = false) @RequestParam(name = "inviteCode", required = false) String inviteCode,
                                        @ApiParam(value = "driver profile", required = true) @RequestParam(name = "profile", required = false) MultipartFile zipFile) throws LogicException, Exception {
        MsgWrapper msgWrapper;
        User checked = userService.findByMobileAndTypeAndStatusIn(mobile, Const.USER.TYPE_DRIVER, Arrays.asList(Const.USER.STATUS_ON, Const.USER.STATUS_UNAPPROVED));
        if (Objects.isNull(checked)) {
            String socialId = null;
            String email = null;
            if (!StringUtils.isEmpty(socialToken)) {
                User verify = googleVerifyToken.verify(socialToken);
                if (Objects.isNull(verify)) {
                    return new MsgWrapper(Const.WS.NOK, "invalid social token");
                } else {
                    email = verify.getEmail();
                    socialId = verify.getSocialId();
                }
            }

            // check social id
            User socialUser = userService.findBySocialIdAndEmailAndType(socialId, email, Const.USER.TYPE_DRIVER);
            if (Objects.nonNull(socialUser)){
                return new MsgWrapper(Const.WS.NOK, "the socialId has already existed");
            }

            com.an.common.bean.UserService service = new com.an.common.bean.UserService();
            service.setServiceGroupId(serviceGroupId);
            service.setProductId(productId);
            service.setVehicleNo(vehicleNo);
            service.setVehicleType(vehicleType);
            try {
                User user = userService.createDriver(mobile, email, socialId, fullname, fcmToken, province, inviteCode, zipFile,  service);
                if (Objects.nonNull(user)){
                    String token = authService.autoSignIn("Basic YW5nbzphbmdvQDIwMTk=", Const.USER.GOOGLE_USER_PREFIX+socialToken+"@1", socialToken, "password");
                    msgWrapper = new MsgWrapper(token, Const.WS.OK, "");
                } else {
                    msgWrapper = new MsgWrapper(user, Const.WS.OK, "");
                }
            } catch (Exception ex){
                if (ex instanceof LogicException){
                    msgWrapper = new MsgWrapper(Const.WS.NOK, ex.getMessage());
                } else {
                    throw ex;
                }
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "the mobile has already existed");
        }

        return msgWrapper;
    }

    @GetMapping("/api/findUserService")
    @LogsActivityAnnotation
    @ApiOperation("function to user service by driver id and service id")
    public MsgWrapper findUserService (@ApiParam(value = "userId", required = true) @RequestParam("userId") Long userId,
                                       @ApiParam(value = "serviceId", required = true) @RequestParam("serviceId") Long serviceId
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userId) && Objects.nonNull(serviceId)){
            com.an.common.bean.UserService userService = userServiceService.findByUserIdAndServiceId(userId, serviceId);
            msgWrapper = new MsgWrapper(userService, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid param");
        }
        return msgWrapper;
    }

    @PostMapping("/api/registerService")
    @LogsActivityAnnotation
    @ApiOperation("function to register service for driver")
    public MsgWrapper registerService (@ApiParam(value = "userId", required = true) @RequestParam("userId") Long userId,
                                       @ApiParam(value = "mobile", required = true) @RequestParam("mobile") String mobile,
                                       @ApiParam(value = "serviceId", required = true) @RequestParam("serviceId") Long serviceId,
                                       @ApiParam(value = "productId") @RequestParam(name = "productId", required = false) Long productId,
                                       @ApiParam(value = "vehicleNo", required = true) @RequestParam("vehicleNo") String vehicleNo,
                                       @ApiParam(value = "vehicleType", required = true) @RequestParam("vehicleType") String vehicleType,
                                       @ApiParam(value = "driver profile") @RequestParam(name = "profile", required = false) MultipartFile zipFile
                                       ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.isNull(userId) || Objects.isNull(serviceId) || StringUtils.isEmpty(vehicleNo) || StringUtils.isEmpty(vehicleType)) {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        } else {
            com.an.common.bean.UserService checked = userServiceService.findByUserIdAndServiceId(userId, serviceId);
            if (Objects.isNull(checked)){
                // save user
                com.an.common.bean.UserService userService = userServiceService.registerUserService(userId, serviceId, productId, vehicleNo, vehicleType);
                if (Objects.nonNull(userService)) {
                    String message = "successfully";
                    // store driver profile
                    if (Objects.nonNull(zipFile)){
                        List<UserServiceProfile> lstUserServiceProfiles = userServiceProfileService.uploadUserServiceProfile(userService.getUserServiceId(), mobile, zipFile);
                        if (lstUserServiceProfiles == null || lstUserServiceProfiles.isEmpty()){
                            message += ", upload driver profile fail";
                        }
                    }

                    // return result
                    msgWrapper = new MsgWrapper(Const.WS.OK, message);
                    msgWrapper.setWrapper(userService);
                } else {
                    msgWrapper = new MsgWrapper(Const.WS.NOK, "the system has error when register new service");
                }
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "the mobile has already existed");
            }
        }
        return msgWrapper;
    }

    @PostMapping("/api/approvedService")
    @LogsActivityAnnotation
    @ApiOperation("function to approved user request")
    public MsgWrapper approvedService (@ApiParam(value = "userServiceId", required = true) @RequestParam("userServiceId") Long userServiceId,
                                      @ApiParam(value = "status", required = true) @RequestParam("status") String status,
                                      @ApiParam(value = "description") @RequestParam(name = "description", required = false) String description
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userServiceId) && Objects.nonNull(status)){
            com.an.common.bean.UserService entity = userServiceService.approvedService(userServiceId, status, description);
            if (Objects.nonNull(entity)){
                msgWrapper = new MsgWrapper(entity, Const.WS.OK, "successfully");
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "the system has error when approved user request");
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/updateLocation")
    @LogsActivityAnnotation
    @ApiOperation("function to update driver location")
    public MsgWrapper updateLocation (@ApiParam(value = "userId", required = true) @RequestParam("userId") Long userId,
                                      @ApiParam(value = "lstGroupServiceId", required = true) @RequestParam("lstGroupServiceId") List<Long> lstGroupServiceId,
                                      @ApiParam(value = "token", required = false) @RequestParam(value = "token", required = false) String token,
                                      @ApiParam(value = "x", required = true) @RequestParam("x") Double x,
                                      @ApiParam(value = "y", required = true) @RequestParam("y") Double y
                                      ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userId) && Objects.nonNull(lstGroupServiceId) && Objects.nonNull(x) && Objects.nonNull(y)){
            List<UserLocationEntity> lstEntity = new ArrayList<>();
            if (!lstGroupServiceId.isEmpty()) {
                lstGroupServiceId.forEach(z->{
                    UserLocationEntity entity = null;
                    if (Objects.equals(Const.SERVICE_GROUP.BIKE, z)) {
                        entity = userBikeLocationService.updateUserLocation(userId, token, x, y);
                    } else if (Objects.equals(Const.SERVICE_GROUP.CAR, z)) {
                        entity = userCarLocationService.updateUserLocation(userId, token, x, y);
                    } else if (Objects.equals(Const.SERVICE_GROUP.CAR7, z)) {
                        entity = userCar7LocationService.updateUserLocation(userId, token, x, y);
                    } else if (Objects.equals(Const.SERVICE_GROUP.TRUCK, z)) {
                        entity = userTruckLocationService.updateUserLocation(userId, token, x, y);
                    }
                    if (Objects.nonNull(entity)){
                        lstEntity.add(entity);
                    }
                });
            }
            if (!lstEntity.isEmpty()){
                msgWrapper = new MsgWrapper(Const.WS.OK, "successfully");
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "the system has error when updated user location");
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/removeLocation")
    @LogsActivityAnnotation
    @ApiOperation("function to remove driver location")
    public MsgWrapper removeLocation (@ApiParam(value = "userId", required = true) @RequestParam("userId") Long userId,
                                      @ApiParam(value = "lstGroupServiceId", required = true) @RequestParam("lstGroupServiceId") List<Long> lstGroupServiceId
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userId) && Objects.nonNull(lstGroupServiceId)){
            if (!lstGroupServiceId.isEmpty()) {
                lstGroupServiceId.forEach(z->{
                    if (Objects.equals(Const.SERVICE_GROUP.BIKE, z)) {
                        userBikeLocationService.evictById(userId);
                    } else if (Objects.equals(Const.SERVICE_GROUP.CAR, z)) {
                        userCarLocationService.evictById(userId);
                    } else if (Objects.equals(Const.SERVICE_GROUP.CAR7, z)) {
                        userCar7LocationService.evictById(userId);
                    } else if (Objects.equals(Const.SERVICE_GROUP.TRUCK, z)) {
                        userTruckLocationService.evictById(userId);
                    }
                });
            }
            msgWrapper = new MsgWrapper(Const.WS.OK, "successfully");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/updateUserWallet")
    @LogsActivityAnnotation
    @ApiOperation("function to update user wallet")
    public MsgWrapper updateUserWallet (@ApiParam(value = "userWalletId", required = true) @RequestParam("userWalletId") Long userWalletId,
                                        @ApiParam(value = "operate", required = true) @RequestParam("operate") String operate,
                                        @ApiParam(value = "balance", required = true) @RequestParam("balance") Double balance
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userWalletId) && Objects.nonNull(operate) && Objects.nonNull(balance)){
            UserWallet userWallet = userWalletService.updateUserWallet(userWalletId, operate, balance);
            if (Objects.nonNull(userWallet)){
                msgWrapper = new MsgWrapper(Const.WS.OK, "successfully");
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "the system has error when updated user wallet");
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/api/getUserWalletByType")
    @LogsActivityAnnotation
    @ApiOperation("function to get user wallet by type")
    public MsgWrapper getUserWalletByType (@ApiParam(value = "userId", required = true) @RequestParam("userId") Long userId,
                                           @ApiParam(value = "type", required = true) @RequestParam("type") String type
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userId) && Objects.nonNull(type)){
            UserWallet userWallet = userWalletService.findByUserIdAndType(userId, type);
            msgWrapper = new MsgWrapper(userWallet, Const.WS.OK, "successfully");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/api/registerProduct")
    @LogsActivityAnnotation
    @ApiOperation("function to register product")
    public MsgWrapper registerProduct (@ApiParam(value = "userServiceId", required = true) @RequestParam("userServiceId") Long userServiceId,
                                       @ApiParam(value = "productId", required = true) @RequestParam("productId") Long productId
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userServiceId) && Objects.nonNull(productId)){
            try {
                userServiceService.registerProduct(userServiceId, productId);
                msgWrapper = new MsgWrapper(Const.WS.OK, "");
            } catch (Exception ex){
                if (ex instanceof LogicException){
                    logger.error(ex.getMessage(), ex);
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

    @GetMapping("/api/getUserByLocation")
    @LogsActivityAnnotation
    @ApiOperation("function to get driver by location")
    public MsgWrapper getUserByLocation (@ApiParam(value = "serviceGroupId", required = true) @RequestParam("serviceGroupId") Long serviceGroupId,
                                         @ApiParam(value = "x", required = true) @RequestParam("x") Double x,
                                         @ApiParam(value = "y", required = true) @RequestParam("y") Double y
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(x) && Objects.nonNull(y)) {
            List<UserLocation> lst = new ArrayList<>();
            if (Objects.equals(Const.SERVICE_GROUP.BIKE, serviceGroupId)){
                lst = userBikeLocationService.findTop10UserByLocationAndDistance(x, y, Const.USER_LOCATION.DEFAULT_DISTANCE);
            } else if (Objects.equals(Const.SERVICE_GROUP.CAR, serviceGroupId)){
                lst = userCarLocationService.findTop10UserByLocationAndDistance(x, y, Const.USER_LOCATION.DEFAULT_DISTANCE);
            } else if (Objects.equals(Const.SERVICE_GROUP.CAR7, serviceGroupId)){
                lst = userCar7LocationService.findTop10UserByLocationAndDistance(x, y, Const.USER_LOCATION.DEFAULT_DISTANCE);
            } else if (Objects.equals(Const.SERVICE_GROUP.TRUCK, serviceGroupId)){
                lst = userTruckLocationService.findTop10UserByLocationAndDistance(x, y, Const.USER_LOCATION.DEFAULT_DISTANCE);
            }
            msgWrapper = new MsgWrapper(lst, Const.WS.OK, "successfully");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/api/getAvatar")
    @ApiOperation("function to get user's avatar")
    public ResponseEntity<Resource> getAvatar(@ApiParam(value = "id of user", required = true) @RequestParam("userId") Long userId) throws Exception {
        User user = userService.findById(userId);
        if (Objects.nonNull(user) && !StringUtils.isEmpty(user.getAvatar())) {
            File file = new File(user.getAvatar());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        }
        return null;
    }

    @PostMapping("/api/updateFcmToken")
    @LogsActivityAnnotation
    @ApiOperation("function to update user fcm token")
    public MsgWrapper updateFcmToken (@ApiParam(value = "userId", required = true) @RequestParam("userId") Long userId,
                                        @ApiParam(value = "fcmToken", required = true) @RequestParam("fcmToken") String fcmToken
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userId) && !StringUtils.isEmpty(fcmToken)){
            User user = userService.updateFcmToken(userId, fcmToken);
            if (Objects.nonNull(user)){
                msgWrapper = new MsgWrapper(Const.WS.OK, "successfully");
            } else {
                msgWrapper = new MsgWrapper(Const.WS.NOK, "the system has error when updated user fcm token");
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/api/getDriverByProvince")
    @LogsActivityAnnotation
    @ApiOperation("function to get driver by province")
    public MsgWrapper getDriverByProvince (@ApiParam(value = "province", required = true) @RequestParam("province") String province) throws Exception {
        MsgWrapper msgWrapper;
        if (!StringUtils.isEmpty(province)) {
            List<UserLocation> lst = new ArrayList<>();
            msgWrapper = new MsgWrapper(userService.getByProvinceAndTypeAndStatus(province, Const.USER.TYPE_DRIVER, Const.USER.STATUS_ON), Const.WS.OK, "successfully");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/api/getDriverLocation")
    @LogsActivityAnnotation
    @ApiOperation("function to get driver location")
    public MsgWrapper getDriverLocation (@ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId,
                                         @ApiParam(value = "serviceGroupId", required = true) @RequestParam("serviceGroupId") Long serviceGroupId
                                         ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(driverId) && Objects.nonNull(serviceGroupId)) {
            UserLocation userLocation = null;
            if (Objects.equals(Const.SERVICE_GROUP.BIKE, serviceGroupId)){
                userLocation = userBikeLocationService.findById(driverId);
            } else if (Objects.equals(Const.SERVICE_GROUP.CAR, serviceGroupId)){
                userLocation = userCarLocationService.findById(driverId);
            } else if (Objects.equals(Const.SERVICE_GROUP.CAR7, serviceGroupId)){
                userLocation = userCar7LocationService.findById(driverId);
            } else if (Objects.equals(Const.SERVICE_GROUP.TRUCK, serviceGroupId)){
                userLocation = userTruckLocationService.findById(driverId);
            }
            msgWrapper = new MsgWrapper(userLocation, Const.WS.OK, "successfully");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

}
