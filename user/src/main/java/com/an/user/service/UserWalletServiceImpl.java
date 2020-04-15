package com.an.user.service;

import com.an.common.bean.UserWallet;
import com.an.common.exception.LogicException;
import com.an.common.utils.Const;
import com.an.user.entity.UserWalletEntity;
import com.an.user.repository.UserRepository;
import com.an.user.repository.UserWalletRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserWalletServiceImpl implements UserWalletService {

    private static Logger logger = LoggerFactory.getLogger(UserWalletService.class);

    @Autowired
    UserWalletRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<UserWallet> findByUserId(Long userId) {
        List<UserWallet> output = new ArrayList<>();
        List<UserWalletEntity> lst = repository.findByUserId(userId);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x, UserWallet.class)));
        }
        return output;
    }

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
    public UserWallet initUserWallet(Long userId, String type, Double balance) {
        UserWallet userWallet = findByUserIdAndType(userId, type);
        if (Objects.isNull(userWallet)) {
            UserWalletEntity entity = new UserWalletEntity();
            entity.setUserId(userId);
            entity.setType(type);
            if (Objects.nonNull(balance)) {
                entity.setBalance(balance);
            } else {
                entity.setBalance(0D);
            }
            entity.setCreateDatetime(userRepository.getSysdate());

            entity = repository.save(entity);
            if (Objects.nonNull(entity)) {
                return modelMapper.map(entity, UserWallet.class);
            }
        } else {
            userWallet.setBalance(userWallet.getBalance()+balance);
            userWallet.setUpdateDatetime(userRepository.getSysdate());
            UserWalletEntity entity  = modelMapper.map(userWallet, UserWalletEntity.class);
            entity = repository.save(entity);
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, UserWallet.class);
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserWallet> initWalletWhenCreateUser(Long userId) {
        List<UserWallet> output = new ArrayList<>();
        List<String> lstBalanceType = Arrays.asList(Const.USER_WALLET.BALANCE_TYPE_DEPOSIT, Const.USER_WALLET.BALANCE_TYPE_PROMOTION, Const.USER_WALLET.BALANCE_TYPE_DISTANCE);
        lstBalanceType.forEach(x->output.add(initUserWallet(userId, x, 0D)));
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

    @Override
    public void minusMoney (Long userId, Double balance) throws LogicException, Exception {
        List<UserWallet> lst = findByUserId(userId);
        if (Objects.nonNull(lst) && !lst.isEmpty()){
            // minus promotion balance first
            Double value = balance;
            for (UserWallet userWallet : lst) {
                if (Objects.equals(userWallet.getType(), Const.USER_WALLET.BALANCE_TYPE_PROMOTION) && userWallet.getBalance() > 0L) {
                    if (userWallet.getBalance() > value) {
                        userWallet.setBalance(userWallet.getBalance() - value);
                        value = 0D;
                    } else {
                        value = value - userWallet.getBalance();
                        userWallet.setBalance(0D);
                    }
                    userWallet.setUpdateDatetime(userRepository.getSysdate());
                    repository.save(modelMapper.map(userWallet, UserWalletEntity.class));
                    break;
                }
            }
            if (value > 0){ // if promotion balance < value ==> continuous minus deposit balance
                for (UserWallet userWallet : lst) {
                    if (Objects.equals(userWallet.getType(), Const.USER_WALLET.BALANCE_TYPE_DEPOSIT) && userWallet.getBalance() > 0L) {
                        if (userWallet.getBalance() >= value) {
                            userWallet.setBalance(userWallet.getBalance() - value);
                            userWallet.setUpdateDatetime(userRepository.getSysdate());
                            repository.save(modelMapper.map(userWallet, UserWalletEntity.class));
                        } else {
                            throw new LogicException(Const.WS.NOK, "Your balance is not enough money");
                        }
                        break;
                    }
                }
            }
        } else {
            throw new LogicException(Const.WS.NOK, "You have not activated wallet yet");
        }
    }
}
