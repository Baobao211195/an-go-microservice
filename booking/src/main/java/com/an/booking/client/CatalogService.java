package com.an.booking.client;

import com.an.booking.fallback.CatalogFallbackFactory;
import com.an.common.bean.MsgWrapper;
import com.an.common.bean.Service;
import com.an.common.bean.UserPromotion;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "catalog-service", fallbackFactory = CatalogFallbackFactory.class)
public interface CatalogService {

    @RequestMapping(path = "/catalog/getServiceById", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<Service> getServiceById(@ApiParam(value = "service_id", required = true) @RequestParam("serviceId") Long serviceId) throws Exception;

    @RequestMapping(path = "/catalog/reduceUserPromotionTimes", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public MsgWrapper<UserPromotion> reduceUserPromotionTimes(@ApiParam(value = "user_promotion_id", required = true) @RequestParam("userPromotionId") Long userPromotionId) throws Exception;

    @RequestMapping(path = "/catalog/getUserPromotionById", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<UserPromotion> getUserPromotionById(@ApiParam(value = "user_promotion_id", required = true) @RequestParam("userPromotionId") Long userPromotionId) throws Exception;
}
