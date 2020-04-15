package com.an.user.client;

import com.an.user.fallback.AuthServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", fallbackFactory = AuthServiceFallbackFactory.class)
public interface AuthService {
    @RequestMapping(path = "/oauth/token", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    String autoSignIn(@RequestHeader("Authorization") String authorization, @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("grant_type") String grantType);
}
