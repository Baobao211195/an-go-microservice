package com.an.catalog.service;

import com.an.catalog.entity.PromotionEntity;
import com.an.catalog.repository.PromotionRepository;
import com.an.common.bean.Promotion;
import com.an.common.utils.Const;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    PromotionRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Date getSysdate() {
        return repository.getSysdate();
    }

    @Override
    @Cacheable(cacheNames = "promotion", key = "#promotionId", unless = "#result == null")
    public Promotion getPromotionById(Long promotionId) {
        Optional<PromotionEntity> optional = repository.findById(promotionId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            return modelMapper.map(optional.get(), Promotion.class);
        }
        return null;
    }

    @Override
    @Cacheable(cacheNames = "promotions", key = "#serviceId", unless = "#result == null")
    public List<Promotion> findPromotionByServiceId(Long serviceId) {
        List<Promotion> output = new ArrayList<>();
        List<PromotionEntity> lst = repository.findByServiceIdAndStatus(serviceId, Const.PROMOTION.STATUS_ON);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x, Promotion.class)));
        }
        return output;
    }

    @Override
    @Cacheable(cacheNames = "userPromotion", key = "#serviceId, #userId", unless = "#result == null")
    public List<Promotion> findByUserIdAndServiceId(Long serviceId, Long userId) {
        List<Promotion> output = new ArrayList<>();
        List<PromotionEntity> lst = repository.findByUserIdAndServiceId(serviceId, userId);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x, Promotion.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Promotion createPromotion(Long serviceId, String name, String content, String type, Long value, Long maxValue, Date startDatetime, Date endDatetime) {
        PromotionEntity entity = new PromotionEntity();
        entity.setServiceId(serviceId);
        entity.setName(name);
        entity.setContent(content);
        entity.setType(type);
        entity.setValue(value);
        entity.setMaxValue(maxValue);
        entity.setStartDatetime(startDatetime);
        entity.setEndDatetime(endDatetime);
        entity.setStatus(Const.PRODUCT.STATUS_ON);
        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, Promotion.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Promotion updatePromotion(Promotion promotion) {
        PromotionEntity entity = modelMapper.map(promotion, PromotionEntity.class);
        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, Promotion.class);
        }
        return null;
    }
}
