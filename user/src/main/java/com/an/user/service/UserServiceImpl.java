package com.an.user.service;

import com.an.common.bean.MsgWrapper;
import com.an.common.bean.User;
import com.an.common.exception.LogicException;
import com.an.common.utils.Const;
import com.an.common.utils.FileUtils;
import com.an.common.utils.RandomUtils;
import com.an.user.client.CatalogService;
import com.an.user.config.AppConfig;
import com.an.user.entity.UserEntity;
import com.an.user.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AppConfig appConfig;

    @Autowired
    UserWalletService userWalletService;

    @Autowired
    UserServiceService userServiceService;

    @Autowired
    UserServiceProfileService userServiceProfileService;

    @Autowired
    CatalogService catalogService;

    @Autowired
    UserInviteService userInviteService;

    @Override
    public Date getSysdate() {
        return repository.getSysdate();
    }

    @Override
    public User findById(Long userId) {
        UserEntity userEntity = repository.getByUserId(userId);
        if (Objects.nonNull(userEntity)){
            return modelMapper.map(userEntity, User.class);
        }
        return null;
    }

    @Override
    public User findBySocialIdAndEmailAndType(String socialId, String email, String type) {
        UserEntity userEntity = repository.getBySocialIdAndEmailAndType(socialId, email, type);
        if (Objects.nonNull(userEntity)){
            return modelMapper.map(userEntity, User.class);
        }
        return null;
    }

    @Override
    public User findByMobileAndStatus(String mobile, String status) {
        UserEntity userEntity = repository.getByMobileAndStatus(mobile, status);
        if (Objects.nonNull(userEntity)){
            return modelMapper.map(userEntity, User.class);
        }
        return null;
    }

    @Override
    public User findByMobileAndStatusIn(String mobile, List<String> lstStatus) {
        UserEntity userEntity = repository.getByMobileAndStatusIn(mobile, lstStatus);
        if (Objects.nonNull(userEntity)){
            return modelMapper.map(userEntity, User.class);
        }
        return null;
    }

    @Override
    public User findByMobileAndTypeAndStatusIn(String mobile, String type, List<String> lstStatus) {
        UserEntity userEntity = repository.getByMobileAndTypeAndStatusIn(mobile, type, lstStatus);
        if (Objects.nonNull(userEntity)){
            return modelMapper.map(userEntity, User.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User createUser(String fullname, String mobile, String email, String avatar, String type, String socialId, String fcmToken, String province, String inviteCode) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFullname(fullname);
        userEntity.setEmail(email);
        userEntity.setSocialId(socialId);
        userEntity.setMobile(mobile);
        userEntity.setAvatar(avatar);
        userEntity.setFcmToken(fcmToken);
        userEntity.setType(type);
        userEntity.setProvince(province);
        userEntity.setInviteCode(RandomUtils.generatePassword(32));
        userEntity.setStatus(Const.USER.STATUS_ON);

        userEntity = repository.save(userEntity);
        if (Objects.nonNull(userEntity)){
            User user = modelMapper.map(userEntity, User.class);
            if (Objects.equals(type, Const.USER.TYPE_CUSTOMER)){
                userWalletService.initWalletWhenCreateUser(user.getUserId());
            }
            if (!StringUtils.isEmpty(inviteCode)){
                userInviteService.insertUserInvite(user.getUserId(), inviteCode);
            }
            return user;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User approvedDriver(Long userId, boolean isApproved, String reason, String staffCode) {
        return null;
    }

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
    @Transactional(rollbackFor = Exception.class)
    public User updateFcmToken(Long userId, String fcmToken) {
        Optional optional = repository.findById(userId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            UserEntity userEntity = (UserEntity) optional.get();
            if (Objects.nonNull(userEntity)){
                userEntity.setFcmToken(fcmToken);
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
    @Transactional(rollbackFor = Exception.class)
    public User updateAvatarUser (Long userId, MultipartFile file) {
        User user = findById(userId);
        if (Objects.nonNull(user)){
            String filename = FileUtils.storeFile(appConfig.getProfileDir()+"/"+userId+"_"+user.getMobile()+"/avatar", file);
            if (!StringUtils.isEmpty(filename)){
                user.setAvatar(filename);
                user.setUpdateDatetime(repository.getSysdate());
                UserEntity userEntity = repository.save(modelMapper.map(user, UserEntity.class));
                if (Objects.nonNull(userEntity)){
                    return modelMapper.map(userEntity, User.class);
                }
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User createDriver(String mobile, String email, String socialId, String fullname, String fcmToken, String province, String inviteCode, MultipartFile unzipFile, com.an.common.bean.UserService userService) throws LogicException, Exception {
        User user = createUser(fullname, mobile, email, "", Const.USER.TYPE_DRIVER, socialId, fcmToken, province, inviteCode);

        String targetService = null;
        if (Objects.nonNull(unzipFile)) {
            // unzip file
            String target = appConfig.getProfileDir() + "/" + user.getUserId();
            targetService = target;
            FileUtils.unzipFile(target, unzipFile.getInputStream());
            File[] files = FileUtils.getListFile(target, "jpg", "png");
            if (files != null && files.length > 0) {
                Arrays.stream(files).forEach(file -> {
                    if (Objects.equals("avatar", FilenameUtils.getBaseName(file.getName()))) {
                        try {
                            org.apache.commons.io.FileUtils.moveFileToDirectory(file, new File(target + "/avatar"), true);
                            user.setAvatar(target + "/avatar/" + file.getName());
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                    } else if (Objects.equals("ID1", FilenameUtils.getBaseName(file.getName()))) {
                        try {
                            org.apache.commons.io.FileUtils.moveFileToDirectory(file, new File(target + "/ID"), true);
                            user.setIdNoFront(target + "/ID/" + file.getName());
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                    } else if (Objects.equals("ID2", FilenameUtils.getBaseName(file.getName()))) {
                        try {
                            org.apache.commons.io.FileUtils.moveFileToDirectory(file, new File(target + "/ID"), true);
                            user.setIdNoBack(target + "/ID/" + file.getName());
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                    } else {
                        try {
                            org.apache.commons.io.FileUtils.moveFileToDirectory(file, new File(target + "/" + userService.getServiceId()), true);
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                    }
                });
            }

            // update user
            repository.save(modelMapper.map(user, UserEntity.class));
        }

        // save user service
        MsgWrapper<List<com.an.common.bean.Service>> msgWrapper = catalogService.getServiceByGroupId(userService.getServiceGroupId());
        List<com.an.common.bean.UserService> lstUserService = new ArrayList<>();
        if (Objects.nonNull(msgWrapper) && Objects.nonNull(msgWrapper.getWrapper())) {
            List<Long> lstServiceId = new ArrayList<>();
            msgWrapper.getWrapper().forEach(x->lstServiceId.add(x.getServiceId()));
            lstUserService = userServiceService.registerListUserService(user.getUserId(), lstServiceId, userService.getProductId(), userService.getVehicleNo(), userService.getVehicleType());
        }
        if (Objects.nonNull(lstUserService) && !lstUserService.isEmpty()) {
            if (Objects.nonNull(targetService)) {
                userServiceProfileService.saveUserServiceProfile(userService.getUserServiceId(), targetService + "/" + userService.getServiceId());
            }
        } else {
            throw new LogicException(Const.WS.NOK, "can not register service");
        }

        return user;
    }

    @Override
    public List<User> getByProvinceAndTypeAndStatus(String province, String type, String status) {
        List<User> output = new ArrayList<>();
        List<UserEntity> lst = repository.getByProvinceAndTypeAndStatus(province, type, status);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, User.class)));
        }
        return null;
    }

}
