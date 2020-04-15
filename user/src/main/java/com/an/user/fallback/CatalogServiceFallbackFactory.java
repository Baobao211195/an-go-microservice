package com.an.user.fallback;

import com.an.common.bean.MsgWrapper;
import com.an.common.bean.Product;
import com.an.common.bean.Promotion;
import com.an.common.bean.Service;
import com.an.common.utils.Const;
import com.an.user.client.CatalogService;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CatalogServiceFallbackFactory implements FallbackFactory<CatalogService> {
    @Override
    public CatalogService create(Throwable throwable) {
        return new CatalogService() {
            @Override
            public MsgWrapper<Product> getProductById(Long productId) {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                MsgWrapper<Product> msgWrapper = new MsgWrapper<>(new Product(), Const.WS.OK, "fallback running");
                return msgWrapper;
            }

            @Override
            public MsgWrapper<Promotion> getPromotionById(Long promotionId) {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                MsgWrapper<Promotion> msgWrapper = new MsgWrapper<>(new Promotion(), Const.WS.OK, "fallback running");
                return msgWrapper;
            }

            @Override
            public MsgWrapper getServiceByGroupId(Long serviceGroupId) throws Exception {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                MsgWrapper<Promotion> msgWrapper = new MsgWrapper<>(Const.WS.NOK, "fallback running");
                return msgWrapper;
            }

            @Override
            public MsgWrapper<Service> getServiceById(Long serviceId) throws Exception {
                if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                    System.out.println("call service error: " + throwable.getMessage());
                    // Treat the HTTP 404 status
                }
                System.out.println("fallback running: " + throwable.getMessage());
                MsgWrapper<Service> msgWrapper = new MsgWrapper<>(Const.WS.NOK, "fallback running");
                return msgWrapper;
            }
        };
    }
}
