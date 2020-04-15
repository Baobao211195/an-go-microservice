package com.an.process.service;

import com.an.common.bean.User;
import com.an.process.entity.UserEntity;
import com.an.process.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateRating(Long userId, Double rating) {
        Optional optional = repository.findById(userId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            UserEntity userEntity = (UserEntity) optional.get();
            if (Objects.nonNull(userEntity)){
                userEntity.setRating(rating);
                userEntity.setUpdateDatetime(repository.getSysdate());
                userEntity = repository.save(userEntity);
                if (Objects.nonNull(userEntity)){
                    return modelMapper.map(userEntity, User.class);
                }
            }
        }
        return null;
    }

    @Override
    public User findByInviteCode(String inviteCode) {
        UserEntity entity = repository.findByInviteCode(inviteCode);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, User.class);
        }
        return null;
    }

    @Override
    public User findByUserId(Long userId) {
        Optional<UserEntity> optional = repository.findById(userId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            return modelMapper.map(optional.get(), User.class);
        }
        return null;
    }


}
