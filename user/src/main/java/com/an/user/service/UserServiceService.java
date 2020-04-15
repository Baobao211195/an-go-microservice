package com.an.user.service;

import com.an.common.bean.UserService;
import com.an.common.exception.LogicException;

import java.util.Date;
import java.util.List;

public interface UserServiceService {

    public List<UserService> findByUserId (Long userId);

    public UserService findByUserIdAndServiceId (Long userId, Long serviceId);

    public UserService registerUserService (Long userId, Long serviceId, Long productId, String vehicleNo, String vehicleType);

    public List<UserService> registerListUserService(Long userId, List<Long> lstServiceId, Long productId, String vehicleNo, String vehicleType);

    public UserService approvedService(Long userServiceId, String status, String description);

    public UserService refreshExpireDatetime(Long userServiceId, Date expireDatetime);

    public void registerProduct(Long userServiceId, Long productId) throws LogicException, Exception;

}
