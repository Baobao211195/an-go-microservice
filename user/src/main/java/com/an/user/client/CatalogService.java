package com.an.user.client;

import com.an.common.bean.MsgWrapper;
import com.an.common.bean.Product;
import com.an.common.bean.Promotion;
import com.an.common.bean.Service;
import com.an.user.fallback.CatalogServiceFallbackFactory;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "catalog-service", fallbackFactory = CatalogServiceFallbackFactory.class)
public interface CatalogService {
    @RequestMapping(path = "/catalog/getProductById", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<Product> getProductById(@RequestParam("productId") Long productId);

    @RequestMapping(path = "/catalog/getPromotionById", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<Promotion> getPromotionById(@RequestParam("promotionId") Long promotionId);

    @RequestMapping(path = "/catalog/getServiceByGroupId", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<List<Service>> getServiceByGroupId(@RequestParam("serviceGroupId") Long serviceGroupId) throws Exception;

    @RequestMapping(path = "/catalog/getServiceById", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public MsgWrapper<Service> getServiceById(@ApiParam(value = "service_id", required = true) @RequestParam("serviceId") Long serviceId) throws Exception;
}
