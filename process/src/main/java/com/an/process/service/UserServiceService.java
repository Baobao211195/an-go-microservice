package com.an.process.service;

import com.an.common.bean.UserService;

import java.util.Date;
import java.util.List;

public interface UserServiceService {
    List<UserService> findByStatusAndExpireDatetimeGreaterThan(String status, Date expireDatetime);

}
