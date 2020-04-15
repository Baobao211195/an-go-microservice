package com.an.booking.fallback;

import com.an.booking.client.CatalogService;
import com.an.common.bean.MsgWrapper;
import com.an.common.bean.Service;
import com.an.common.bean.UserPromotion;
import com.an.common.utils.Const;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CatalogFallbackFactory implements FallbackFactory<CatalogService> {
    @Override
    public CatalogService create(Throwable throwable) {
        return new CatalogService() {
            @Override
            @Cacheable(cacheNames = "getServiceById", key = "#serviceId", unless = "#result == null ")
            public MsgWrapper<Service> getServiceById(Long serviceId) throws Exception {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                return new MsgWrapper<>(Const.WS.NOK, "fallback running");
            }

            @Override
            public MsgWrapper<UserPromotion> reduceUserPromotionTimes(Long userPromotionId) throws Exception {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                return new MsgWrapper<>(Const.WS.NOK, "fallback running");
            }

            @Override
            public MsgWrapper<UserPromotion> getUserPromotionById(Long userPromotionId) throws Exception {
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
