package com.an.security.fallback;

import com.an.common.bean.MsgWrapper;
import com.an.common.bean.User;
import com.an.common.utils.Const;
import com.an.security.client.UserService;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserService> {

    @Override
    public UserService create(Throwable throwable) {
        return new UserService() {
            @Override
            public MsgWrapper findByUsername(String username){
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                MsgWrapper<User> msgWrapper = new MsgWrapper<>(Const.WS.OK, "fallback running");
                User user = new User();
                user.setMobile(username);
                user.setPassword(Const.USER.FAKE_PASSWORD);
                msgWrapper.setWrapper(user);
                return msgWrapper;
            }
        };
    }
}
