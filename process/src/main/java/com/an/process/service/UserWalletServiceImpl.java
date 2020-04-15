package com.an.process.service;

import com.an.common.bean.UserWallet;
import com.an.common.exception.LogicException;
import com.an.common.utils.Const;
import com.an.process.entity.UserWalletEntity;
import com.an.process.repository.UserRepository;
import com.an.process.repository.UserWalletRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserWalletServiceImpl implements UserWalletService {

    @Autowired
    UserWalletRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserWallet findByUserIdAndType(Long userId, String type) {
        UserWalletEntity entity = repository.findByUserIdAndType(userId, type);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, UserWallet.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserWallet updateUserWallet(Long userWalletId, String operate, Double balance) throws LogicException, Exception{
        Optional optional = repository.findById(userWalletId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            UserWalletEntity entity = (UserWalletEntity) optional.get();
            if (Const.USER_WALLET.OPERATE_ADDED.equalsIgnoreCase(operate)){
                entity.setBalance(entity.getBalance()+balance);
            } else {
                if (entity.getBalance() >= balance){
                    entity.setBalance(entity.getBalance()-balance);
                } else {
                    throw new LogicException(Const.WS.NOK, "Your balance is not enough money");
                }
            }
            entity.setUpdateDatetime(userRepository.getSysdate());
            entity = repository.save(entity);
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, UserWallet.class);
            }
        }
        return null;
    }
}
