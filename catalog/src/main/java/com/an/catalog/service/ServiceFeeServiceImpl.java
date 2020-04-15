package com.an.catalog.service;

import com.an.catalog.entity.ServiceFeeEntity;
import com.an.catalog.repository.ServiceFeeRepository;
import com.an.common.bean.Promotion;
import com.an.common.bean.ServiceFee;
import com.an.common.bean.ServiceFeeLarge;
import com.an.common.bean.UserPromotion;
import com.an.common.exception.LogicException;
import com.an.common.utils.Const;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ServiceFeeServiceImpl implements ServiceFeeService {

    @Autowired
    ServiceFeeRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserPromotionService userPromotionService;

    @Autowired
    PromotionService promotionService;

    @Autowired
    AppConfigService appConfigService;

    @Override
    public ServiceFee findByServiceIdAndStatus(Long serviceId, String status) {
        List<ServiceFeeEntity> lst = repository.findByServiceIdAndStatus(serviceId, status);
        if (Objects.nonNull(lst) && !lst.isEmpty()){
            return modelMapper.map(lst.get(0), ServiceFee.class);
        }
        return null;
    }

//    @Override
//    public ServiceFee getTripFee(Long serviceId, Long userPromotionId, Double distance, Long minute) throws LogicException, Exception {
//        ServiceFee serviceFee = null;
//        List<ServiceFee> lstServiceFeeLarge = findByServiceIdAndStatus(serviceId, Const.SERVICE_FEE.STATUS_ON);
//        if (Objects.nonNull(lstServiceFeeLarge) && !lstServiceFeeLarge.isEmpty()){
//            if (Objects.nonNull(userPromotionId) && userPromotionId > 0L){
//                UserPromotion userPromotion = userPromotionService.findById(userPromotionId);
//                Promotion promotion;
//                if (Objects.nonNull(userPromotion) && userPromotion.getTimes() > 0L){
//                    promotion = promotionService.getPromotionById(userPromotion.getPromotionId());
//                } else {
//                    throw new LogicException(Const.WS.NOK, "the promotionId is invalid");
//                }
//
//                Double totalFee = 0D;
//                boolean isRushHour = appConfigService.isRushHours();
//                for (ServiceFeeLarge service : lstServiceFeeLarge){
//                    Double fee = isRushHour ? service.getRushFee() : service.getFee();
//                    if(distance + service.getFrom() - service.getTo() > 0){
//                        totalFee += (service.getTo() - service.getFrom()) * fee;
//                        distance = distance + service.getFrom() - service.getTo();
//                    } else {
//                        totalFee += distance*fee;
//                        break;
//                    }
//                }
//
//                if (Objects.nonNull(promotion)){
//                    if (Objects.equals(Const.PROMOTION.TYPE_VALUE, promotion.getType())){
//                        totalFee -= promotion.getValue();
//                    } else {
//                        totalFee -= totalFee*promotion.getValue() > promotion.getMaxValue() ? promotion.getMaxValue() : totalFee*promotion.getValue();
//                    }
//                }
//
//                totalFee = totalFee < 0D ? 0D : totalFee;
//
//                serviceFee = new ServiceFeeLarge();
//                serviceFee.setRushHour(isRushHour);
//                serviceFee.setTotalFee(totalFee);
//            }
//        }
//        return serviceFee;
//    }

    @Override
    public ServiceFee getTripFee(Long serviceId, Long userPromotionId, Double distance, Long minute) throws LogicException, Exception {
        ServiceFee serviceFee = findByServiceIdAndStatus(serviceId, Const.SERVICE_FEE.STATUS_ON);
        if (Objects.nonNull(serviceFee)){
            if (Objects.nonNull(userPromotionId) && userPromotionId > 0L){
                UserPromotion userPromotion = userPromotionService.findById(userPromotionId);
                Promotion promotion;
                if (Objects.nonNull(userPromotion) && userPromotion.getTimes() > 0L){
                    promotion = promotionService.getPromotionById(userPromotion.getPromotionId());
                } else {
                    throw new LogicException(Const.WS.NOK, "the promotionId is invalid");
                }

                Double totalFee = 0D;
                if (distance > serviceFee.getMinDistance()){
                    totalFee += serviceFee.getMinFee() + (distance-serviceFee.getMinDistance())*serviceFee.getNormalFee();
                } else {
                    totalFee += serviceFee.getMinFee();
                }
                if (minute > 0L){
                    totalFee += minute*serviceFee.getTimeFee();
                }
                serviceFee.setFee(totalFee < 0D ? 0D : totalFee);
                if (Objects.nonNull(promotion)){
                    if (Objects.equals(Const.PROMOTION.TYPE_VALUE, promotion.getType())){
                        totalFee -= promotion.getValue();
                    } else {
                        totalFee -= totalFee*promotion.getValue() > promotion.getMaxValue() ? promotion.getMaxValue() : totalFee*promotion.getValue();
                    }
                }

                totalFee = totalFee < 0D ? 0D : totalFee;
                serviceFee.setTotalFee(totalFee);
                serviceFee.setBonus(serviceFee.getFee()-serviceFee.getTotalFee());
            }
        }
        return serviceFee;
    }
}
