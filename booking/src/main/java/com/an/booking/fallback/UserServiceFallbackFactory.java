package com.an.booking.fallback;

import com.an.booking.client.UserService;
import com.an.common.bean.MsgWrapper;
import com.an.common.bean.User;
import com.an.common.bean.UserLocation;
import com.an.common.bean.UserWallet;
import com.an.common.utils.Const;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserService> {
    @Override
    public UserService create(Throwable throwable) {
        return new UserService() {
            @Override
            @Cacheable(value = "getUserById", key = "#userId", unless = "#result == null ")
            public MsgWrapper<User> getUserById(Long userId) throws Exception {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                return new MsgWrapper<>(Const.WS.NOK, "fallback running");
            }

            @Override
            public MsgWrapper<List<UserLocation>> getUserByLocation(Long serviceGroupId, Double x, Double y) throws Exception {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                return new MsgWrapper<>(Const.WS.NOK, "fallback running");
            }

            @Override
            public MsgWrapper<UserWallet> updateUserWallet(Long userWalletId, String operate, Double balance) throws Exception {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                return new MsgWrapper<>(Const.WS.NOK, "fallback running");
            }

            @Override
            public MsgWrapper findUserService(Long userId, Long serviceId) throws Exception {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                return new MsgWrapper<>(Const.WS.NOK, "fallback running");
            }

            @Override
            public MsgWrapper<List<User>> getDriverByProvince(String province) throws Exception {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                return new MsgWrapper<>(Const.WS.NOK, "fallback running");
            }

            @Override
            public MsgWrapper<UserWallet> getUserWalletByType(Long userId, String type) throws Exception {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                return new MsgWrapper<>(Const.WS.NOK, "fallback running");
            }

            @Override
            public MsgWrapper<UserLocation> getDriverLocation(Long driverId, Long serviceGroupId) throws Exception {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                return new MsgWrapper<>(Const.WS.NOK, "fallback running");
            }
        };
    }
}
