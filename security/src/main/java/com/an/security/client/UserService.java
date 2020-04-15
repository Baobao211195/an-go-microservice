package com.an.security.client;

import com.an.common.bean.MsgWrapper;
import com.an.common.bean.User;
import com.an.security.fallback.UserServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", fallbackFactory = UserServiceFallbackFactory.class)
public interface UserService {

    @RequestMapping(path = "/security/findByUsername", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<User> findByUsername(@RequestParam("username") String username);

}
