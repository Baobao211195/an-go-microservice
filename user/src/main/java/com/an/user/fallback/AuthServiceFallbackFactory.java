package com.an.user.fallback;

import com.an.user.client.AuthService;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceFallbackFactory implements FallbackFactory<AuthService> {
    @Override
    public AuthService create(Throwable throwable) {
        return new AuthService() {
            @Override
            public String autoSignIn(String authorization, String username, String password, String grantType) {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                return null;
            }
        };
    }
}
