package com.an.booking.client;

import com.an.booking.fallback.UserServiceFallbackFactory;
import com.an.common.bean.MsgWrapper;
import com.an.common.bean.User;
import com.an.common.bean.UserLocation;
import com.an.common.bean.UserWallet;
import io.swagger.annotations.ApiParam;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service", fallbackFactory = UserServiceFallbackFactory.class)
public interface UserService {

    @RequestMapping(path = "/api/getUserById", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<User> getUserById(@ApiParam(value = "userId", required = true) @RequestParam("userId") Long userId) throws Exception;

    @RequestMapping(path = "/api/getUserByLocation", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<List<UserLocation>> getUserByLocation (@ApiParam(value = "serviceGroupId", required = true) @RequestParam("serviceGroupId") Long serviceGroupId, @ApiParam(value = "x", required = true) @RequestParam("x") Double x, @ApiParam(value = "y", required = true) @RequestParam("y") Double y) throws Exception;

    @RequestMapping(path = "/api/updateUserWallet", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public MsgWrapper<UserWallet> updateUserWallet (@ApiParam(value = "userWalletId", required = true) @RequestParam("userWalletId") Long userWalletId,
                                                    @ApiParam(value = "operate", required = true) @RequestParam("operate") String operate,
                                                    @ApiParam(value = "balance", required = true) @RequestParam("balance") Double balance
    ) throws Exception;

    @RequestMapping(path = "/api/findUserService", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<com.an.common.bean.UserService> findUserService (@ApiParam(value = "userId", required = true) @RequestParam("userId") Long userId,
                                                                       @ApiParam(value = "serviceId", required = true) @RequestParam("serviceId") Long serviceId
    ) throws Exception;

    @RequestMapping(path = "/api/getDriverByProvince", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<List<User>> getDriverByProvince (@ApiParam(value = "province", required = true) @RequestParam("province") String province) throws Exception;

    @RequestMapping(path = "/api/getUserWalletByType", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<UserWallet> getUserWalletByType (@ApiParam(value = "userId", required = true) @RequestParam("userId") Long userId,
                                           @ApiParam(value = "type", required = true) @RequestParam("type") String type
    ) throws Exception;

    @RequestMapping(path = "/api/getDriverLocation", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<UserLocation> getDriverLocation (@ApiParam(value = "driverId", required = true) @RequestParam("driverId") Long driverId,
                                         @ApiParam(value = "serviceGroupId", required = true) @RequestParam("serviceGroupId") Long serviceGroupId
    ) throws Exception;
}
