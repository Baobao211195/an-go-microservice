package com.an.catalog.service;

import com.an.catalog.entity.UserPromotionEntity;
import com.an.catalog.repository.UserPromotionRepository;
import com.an.common.bean.UserPromotion;
import com.an.common.utils.Const;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserPromotionServiceImpl implements UserPromotionService {

    @Autowired
    UserPromotionRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserPromotion insertUserPromotionService(Long userId, Long promotionId, Date startDatetime, Date endDatetime) {
        UserPromotionEntity entity = new UserPromotionEntity();
        entity.setUserId(userId);
        entity.setPromotionId(promotionId);
        entity.setStartDatetime(startDatetime);
        entity.setEndDatetime(endDatetime);
        entity.setStatus(Const.USER_PROMOTION.STATUS_ON);
        entity = repository.save(entity);

        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, UserPromotion.class);
        }
        return null;
    }

    @Override
    public UserPromotion reduceUserPromotionTimes(Long userPromotionId) {
        Optional<UserPromotionEntity> optional = repository.findById(userPromotionId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            UserPromotionEntity entity = optional.get();
            entity.setTimes(entity.getTimes()-1);
            entity = repository.save(entity);
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, UserPromotion.class);
            }
        }
        return null;
    }

    @Override
    public UserPromotion findById(Long userPromotionId) {
        Optional<UserPromotionEntity> optional = repository.findById(userPromotionId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            return modelMapper.map(optional.get(), UserPromotion.class);
        }
        return null;
    }
}
