package com.an.user.service;

import com.an.common.bean.MsgWrapper;
import com.an.common.bean.Product;
import com.an.common.bean.UserService;
import com.an.common.exception.LogicException;
import com.an.common.utils.Const;
import com.an.user.client.CatalogService;
import com.an.user.entity.UserServiceEntity;
import com.an.user.repository.UserServiceRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceServiceImpl implements UserServiceService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceService.class);

    @Autowired
    UserServiceRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    com.an.user.service.UserService userService;

    @Autowired
    CatalogService catalogService;

    @Autowired
    UserWalletService userWalletService;

    @Override
    public List<UserService> findByUserId(Long userId) {
        List<UserService> output = new ArrayList<>();
        List<UserServiceEntity> lst = repository.findByUserIdAndStatus(userId, Const.USER_SERVICE.STATUS_APPROVED);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x, UserService.class)));
        }
        return output;
    }

    @Override
    public UserService findByUserIdAndServiceId(Long userId, Long serviceId) {
        UserServiceEntity entity = repository.findByUserIdAndServiceIdAndStatus(userId, serviceId, Const.USER_SERVICE.STATUS_APPROVED);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, UserService.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserService registerUserService(Long userId, Long serviceId, Long productId, String vehicleNo, String vehicleType) {
        UserServiceEntity entity = new UserServiceEntity();
        entity.setUserId(userId);
        entity.setServiceId(serviceId);
        entity.setProductId(productId);
        entity.setVehicleNo(vehicleNo);
        entity.setVehicleType(vehicleType);
        entity.setCreateDatetime(userService.getSysdate());
        if (Objects.nonNull(productId)) {
            //set expire datetime by product
            MsgWrapper<Product> msgWrapper = catalogService.getProductById(productId);
            if (Objects.nonNull(msgWrapper) && Const.WS.OK.equalsIgnoreCase(msgWrapper.getCode()) && Objects.nonNull(msgWrapper.getWrapper())) {
                Product product = msgWrapper.getWrapper();
                if (Objects.nonNull(product.getDiscount())) {
                    long numofday = 0;
                    if (Const.PRODUCT.TYPE_DAY.equalsIgnoreCase(product.getProductType())) {
                        numofday = Math.round(product.getDiscount() / product.getProductFee());
                    } else if (Const.PRODUCT.TYPE_WEEK.equalsIgnoreCase(product.getProductType())) {
                        numofday = Math.round(product.getDiscount() * 7 / product.getProductFee());
                    } else {
                        numofday = Math.round(product.getDiscount() * 30 / product.getProductFee());
                    }
                    entity.setExpireDatetime(Timestamp.valueOf(LocalDateTime.now().plusDays(numofday)));
                }
            }
        } else { // TODO temp config by the first launch
            entity.setExpireDatetime(Timestamp.valueOf(LocalDateTime.now().plusDays(30L)));
        }
        entity.setStatus(Const.USER_SERVICE.STATUS_NEW);
        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, UserService.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserService> registerListUserService(Long userId, List<Long> lstServiceId, Long productId, String vehicleNo, String vehicleType) {
        List<UserService> output = new ArrayList<>();
        lstServiceId.forEach(x->output.add(registerUserService(userId, x, productId, vehicleNo, vehicleType)));
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserService approvedService(Long userServiceId, String status, String description) {
        Optional<UserServiceEntity> optional = repository.findById(userServiceId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            UserServiceEntity entity = optional.get();
            entity.setStatus(status);
            entity.setDescription(description);
            entity.setUpdateDatetime(userService.getSysdate());
            if (Objects.equals(Const.USER_SERVICE.STATUS_APPROVED, status)){
                //set expire datetime by product
                MsgWrapper<Product> msgWrapper = catalogService.getProductById(entity.getProductId());
                if (Objects.nonNull(msgWrapper) && Const.WS.OK.equalsIgnoreCase(msgWrapper.getCode()) && Objects.nonNull(msgWrapper.getWrapper())) {
                    Product product = msgWrapper.getWrapper();
                    if (Objects.nonNull(product.getDiscount())) {
                        long numofday = 0;
                        if (Const.PRODUCT.TYPE_DAY.equalsIgnoreCase(product.getProductType())) {
                            numofday = Math.round(product.getDiscount() / product.getProductFee());
                        } else if (Const.PRODUCT.TYPE_WEEK.equalsIgnoreCase(product.getProductType())) {
                            numofday = Math.round(product.getDiscount() * 7 / product.getProductFee());
                        } else {
                            numofday = Math.round(product.getDiscount() * 30 / product.getProductFee());
                        }
                        entity.setExpireDatetime(Timestamp.valueOf(LocalDateTime.now().plusDays(numofday)));
                    }

                    // initial user wallet
                    Double startMoney = Objects.nonNull(product.getStartMoney()) ? Double.valueOf(product.getStartMoney()) : 0D;
                    userWalletService.initUserWallet(entity.getUserId(), Const.USER_WALLET.BALANCE_TYPE_DEPOSIT, startMoney);
                }
            }
            entity = repository.save(entity);
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, UserService.class);
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserService refreshExpireDatetime(Long userServiceId, Date expireDatetime) {
        Optional optional = repository.findById(userServiceId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            UserServiceEntity entity = (UserServiceEntity) optional.get();
            entity.setExpireDatetime(expireDatetime);
            entity.setUpdateDatetime(userService.getSysdate());
            entity = repository.save(entity);
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, UserService.class);
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerProduct(Long userServiceId, Long productId) throws LogicException, Exception {
        Optional<UserServiceEntity> optional = repository.findById(userServiceId);
        if (Objects.nonNull(optional) && optional.isPresent()) {
            UserServiceEntity entity = optional.get();
            MsgWrapper<Product> msgWrapper = catalogService.getProductById(productId);
            if (Objects.nonNull(msgWrapper) && Objects.nonNull(msgWrapper.getWrapper())) {
                entity.setProductId(productId);
                Product product = msgWrapper.getWrapper();
                long numofday = 0;
                if (Const.PRODUCT.TYPE_DAY.equalsIgnoreCase(product.getProductType())) {
                    numofday = 1;
                } else if (Const.PRODUCT.TYPE_WEEK.equalsIgnoreCase(product.getProductType())) {
                    numofday = 7;
                } else {
                    numofday = 30;
                }
                if (Objects.nonNull(product.getDiscount())) {
                    if (Const.PRODUCT.TYPE_DAY.equalsIgnoreCase(product.getProductType())){
                        numofday = Math.round(product.getDiscount() / product.getProductFee());
                    } else if (Const.PRODUCT.TYPE_WEEK.equalsIgnoreCase(product.getProductType())) {
                        numofday = Math.round(product.getDiscount() * 7 / product.getProductFee());
                    } else {
                        numofday = Math.round(product.getDiscount() * 30 / product.getProductFee());
                    }
                }
                entity.setExpireDatetime(Timestamp.valueOf(LocalDateTime.now().plusDays(numofday)));

                userWalletService.minusMoney(entity.getUserId(), Double.valueOf(product.getProductFee()));
            } else {
                throw new LogicException(Const.WS.NOK, "invalid product");
            }
        } else {
            throw new LogicException(Const.WS.NOK, "invalid user service");
        }
    }
}
