package com.an.catalog.service;

import com.an.common.bean.*;
import com.an.common.exception.LogicException;
import com.an.common.logger.LogsActivityAnnotation;
import com.an.common.swagger.SwaggerDocument;
import com.an.common.utils.Const;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SwaggerDocument(name = "CATALOG_API")
@Api(value = "Catalog management service", description = "Endpoints for handling catalog management flow")
public class ApiService {
    private static Logger logger = LoggerFactory.getLogger(ApiService.class);

    @Autowired
    ProductService productService;

    @Autowired
    PromotionService promotionService;

    @Autowired
    UserPromotionService userPromotionService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ServiceGroupService serviceGroupService;

    @Autowired
    ServiceFeeService serviceFeeService;

    @Autowired
    DailyTitleService dailyTitleService;

    @GetMapping("/catalog/getProductById")
    @LogsActivityAnnotation
    @ApiOperation("function to get product by id")
    public MsgWrapper getProductById(@ApiParam(value = "product_id", required = true) @RequestParam("productId") Long productId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(productId)) {
            Product product = productService.getProductById(productId);
            msgWrapper = new MsgWrapper(product, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/catalog/getProductByServiceId")
    @LogsActivityAnnotation
    @ApiOperation("function to get list of product")
    public MsgWrapper getProductByServiceId(@ApiParam(value = "service_id", required = true) @RequestParam("serviceId") Long serviceId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(serviceId)) {
            List<Product> lstProduct = productService.findProductByServiceId(serviceId);
            msgWrapper = new MsgWrapper(lstProduct, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/catalog/createProduct")
    @LogsActivityAnnotation
    @ApiOperation("function to create product")
    public MsgWrapper createProduct(@ApiParam(value = "service_id", required = true) @RequestParam("serviceId") Long serviceId,
                                    @ApiParam(value = "product_name", required = true) @RequestParam("productName") String productName,
                                    @ApiParam(value = "product_type", required = true) @RequestParam("productType") String productType,
                                    @ApiParam(value = "product_fee", required = true) @RequestParam("productFee") Long productFee,
                                    @ApiParam(value = "discount", required = true) @RequestParam("discount") Long discount,
                                    @ApiParam(value = "startMoney", required = true) @RequestParam("startMoney") Long startMoney
                                    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(serviceId)) {
            Product product = productService.createProduct(serviceId, productName, productType, productFee, discount, startMoney);
            msgWrapper = new MsgWrapper(product, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/catalog/updateProduct")
    @LogsActivityAnnotation
    @ApiOperation("function to update product")
    public MsgWrapper updateProduct(@ApiParam(value = "product object", required = true) @RequestBody Product product) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(product)) {
            Product output = productService.updateProduct(product);
            msgWrapper = new MsgWrapper(output, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/catalog/getPromotionById")
    @LogsActivityAnnotation
    @ApiOperation("function to get promotion by id")
    public MsgWrapper getPromotionById(@ApiParam(value = "promotion_id", required = true) @RequestParam("promotionId") Long promotionId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(promotionId)) {
            Promotion promotion = promotionService.getPromotionById(promotionId);
            msgWrapper = new MsgWrapper(promotion, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/catalog/getUserPromotionById")
    @LogsActivityAnnotation
    @ApiOperation("function to get user promotion by id")
    public MsgWrapper getUserPromotionById(@ApiParam(value = "user_promotion_id", required = true) @RequestParam("userPromotionId") Long userPromotionId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userPromotionId)) {
            UserPromotion userPromotion = userPromotionService.findById(userPromotionId);
            msgWrapper = new MsgWrapper(userPromotion, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/catalog/getPromotionByUser")
    @LogsActivityAnnotation
    @ApiOperation("function to get list of promotion by user")
    public MsgWrapper getPromotionByUser(@ApiParam(value = "user_id", required = true) @RequestParam("userId") Long userId,
                                         @ApiParam(value = "service_id", required = true) @RequestParam("serviceId") Long serviceId
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(serviceId)) {
            List<Promotion> lstPromotion = promotionService.findByUserIdAndServiceId(serviceId, userId);
            msgWrapper = new MsgWrapper(lstPromotion, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/catalog/reduceUserPromotionTimes")
    @LogsActivityAnnotation
    @ApiOperation("function to reduce user promotion times")
    public MsgWrapper reduceUserPromotionTimes(@ApiParam(value = "user_promotion_id", required = true) @RequestParam("userPromotionId") Long userPromotionId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userPromotionId)) {
            msgWrapper = new MsgWrapper(userPromotionService.reduceUserPromotionTimes(userPromotionId), Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/catalog/createPromotion")
    @LogsActivityAnnotation
    @ApiOperation("function to create promotion")
    public MsgWrapper createPromotion(@ApiParam(value = "service_id", required = true) @RequestParam("serviceId") Long serviceId,
                                    @ApiParam(value = "name", required = true) @RequestParam("name") String name,
                                    @ApiParam(value = "content", required = true) @RequestParam("content") String content,
                                    @ApiParam(value = "type", required = true) @RequestParam("type") String type,
                                    @ApiParam(value = "value", required = true) @RequestParam("value") Long value,
                                    @ApiParam(value = "max_value", required = true) @RequestParam("maxValue") Long maxValue,
                                    @ApiParam(value = "start_datetime", required = true) @RequestParam("startDatetime") Date startDatetime,
                                    @ApiParam(value = "end_datetime", required = true) @RequestParam("endDatetime") Date endDatetime
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(serviceId)) {
            Promotion promotion = promotionService.createPromotion(serviceId, name, content, type, value, maxValue, startDatetime, endDatetime);
            msgWrapper = new MsgWrapper(promotion, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/catalog/updatePromotion")
    @LogsActivityAnnotation
    @ApiOperation("function to update promotion")
    public MsgWrapper updatePromotion(@ApiParam(value = "promotion object", required = true) @RequestBody Promotion promotion) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(promotion)) {
            Promotion output = promotionService.updatePromotion(promotion);
            msgWrapper = new MsgWrapper(output, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/catalog/insertUserPromotion")
    @LogsActivityAnnotation
    @ApiOperation("function to insert user promotion")
    public MsgWrapper insertUserPromotion(@ApiParam(value = "user_id", required = true) @RequestParam("userId") Long userId,
                                          @ApiParam(value = "promotion_id", required = true) @RequestParam("promotionId") Long promotionId,
                                          @ApiParam(value = "start_datetime", required = true) @RequestParam("startDatetime") Date startDatetime,
                                          @ApiParam(value = "end_datetime", required = true) @RequestParam("endDatetime") Date endDatetime
    ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(userId) && Objects.nonNull(promotionId) && Objects.nonNull(startDatetime)) {
            UserPromotion userPromotion = userPromotionService.insertUserPromotionService(userId, promotionId, startDatetime, endDatetime);
            msgWrapper = new MsgWrapper(userPromotion, Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @PostMapping("/catalog/createNotification")
    @LogsActivityAnnotation
    public MsgWrapper createNotification(@ApiParam(value = "name", required = true) @RequestParam("name") String name,
                                      @ApiParam(value = "content", required = true) @RequestParam("content") String content,
                                      @ApiParam(value = "type", required = true) @RequestParam("type") String type,
                                      @ApiParam(value = "push_datetime", required = true) @RequestParam("pushDatetime") Date pushDatetime
    ) throws Exception {
        return new MsgWrapper(notificationService.createNotification(name, content, type, pushDatetime), Const.WS.OK, "");
    }

    @PostMapping("/catalog/updateNotification")
    @LogsActivityAnnotation
    @ApiOperation("function to update promotion")
    public MsgWrapper updateNotification(@ApiParam(value = "notification_id", required = true) @RequestParam("notificationId") Long notificationId,
                                      @ApiParam(value = "name") @RequestParam(value = "name", required = false) String name,
                                      @ApiParam(value = "content") @RequestParam(name = "content", required = false) String content,
                                      @ApiParam(value = "type") @RequestParam(name = "type", required = false) String type,
                                      @ApiParam(value = "status") @RequestParam(name = "status", required = false) String status,
                                      @ApiParam(value = "push_datetime") @RequestParam(name = "pushDatetime", required = false) Date pushDatetime) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(notificationId)) {
            try {
                Notification output = notificationService.updateNotification(notificationId, name, content, type, pushDatetime, status);
                msgWrapper = new MsgWrapper(output, Const.WS.OK, "");
            } catch (Exception ex) {
                msgWrapper = new MsgWrapper(Const.WS.NOK, ex.getMessage());
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/catalog/getServiceByGroupId")
    @LogsActivityAnnotation
    @ApiOperation("function to get service by group id")
    public MsgWrapper getServiceByGroupId(@ApiParam(value = "service_group_id", required = true) @RequestParam("serviceGroupId") Long serviceGroupId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(serviceGroupId)) {
            msgWrapper = new MsgWrapper(serviceGroupService.findServiceByServiceGroupId(serviceGroupId), Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/catalog/getServiceById")
    @LogsActivityAnnotation
    @ApiOperation("function to get service by id")
    public MsgWrapper getServiceById(@ApiParam(value = "service_id", required = true) @RequestParam("serviceId") Long serviceId) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(serviceId)) {
            msgWrapper = new MsgWrapper(serviceGroupService.findServiceById(serviceId), Const.WS.OK, "");
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/catalog/getTripFee")
    @LogsActivityAnnotation
    @ApiOperation("function to get fee of trip")
    public MsgWrapper getTripFee(@ApiParam(value = "service_id", required = true) @RequestParam("serviceId") Long serviceId,
                                 @ApiParam(value = "user_promotion_id", required = false) @RequestParam(value = "userPromotionId", required = false) Long userPromotionId,
                                 @ApiParam(value = "distance", required = true) @RequestParam("distance") Double distance,
                                 @ApiParam(value = "minute", required = true) @RequestParam("minute") Long minute
                                 ) throws Exception {
        MsgWrapper msgWrapper;
        if (Objects.nonNull(serviceId) && Objects.nonNull(distance) && Objects.nonNull(minute)) {
            try {
                msgWrapper = new MsgWrapper(serviceFeeService.getTripFee(serviceId, userPromotionId, distance, minute), Const.WS.OK, "");
            } catch (Exception ex) {
                if (ex instanceof LogicException){
                    msgWrapper = new MsgWrapper(Const.WS.NOK, ex.getMessage());
                } else {
                    throw ex;
                }
            }
        } else {
            msgWrapper = new MsgWrapper(Const.WS.NOK, "invalid parameter");
        }
        return msgWrapper;
    }

    @GetMapping("/security/getDailyTitle")
    @LogsActivityAnnotation
    @ApiOperation("function to get daily title")
    public MsgWrapper getDailyTitle() throws Exception {
        return new MsgWrapper(dailyTitleService.findCurrentDailyTitle(), Const.WS.OK, "");
    }
}
