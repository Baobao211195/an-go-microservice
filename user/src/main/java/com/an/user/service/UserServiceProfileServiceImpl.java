package com.an.user.service;

import com.an.common.bean.UserServiceProfile;
import com.an.common.utils.Const;
import com.an.common.utils.FileUtils;
import com.an.user.config.AppConfig;
import com.an.user.entity.UserServiceProfileEntity;
import com.an.user.repository.UserServiceProfileRepository;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceProfileServiceImpl implements UserServiceProfileService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceProfileService.class);

    @Autowired
    AppConfig appConfig;

    @Autowired
    UserServiceProfileRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<UserServiceProfile> findByUserServiceId(Long userServiceId) {
        List<UserServiceProfile> output = new ArrayList<>();
        List<UserServiceProfileEntity> lst = repository.getByUserServiceId(userServiceId);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(userServiceProfileEntity -> output.add(modelMapper.map(userServiceProfileEntity, UserServiceProfile.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserServiceProfile> uploadUserServiceProfile(Long userServiceId, String mobile, MultipartFile unzipFile) {
        List<UserServiceProfile> output = new ArrayList<>();
        try {
            String target = appConfig.getProfileDir() + "/" + mobile + "_" + userServiceId + "/document";

            // unzip file
            FileUtils.unzipFile(target, unzipFile.getInputStream());

            // insert user profile
            File[] files = FileUtils.getListFile(target, "jpg", "png");
            if (files != null && files.length > 0){
                Arrays.stream(files).forEach(file -> {
                    UserServiceProfileEntity userServiceProfileEntity = new UserServiceProfileEntity();
                    userServiceProfileEntity.setUserServiceId(userServiceId);
                    userServiceProfileEntity.setProfileValue(target+"/"+file.getName());
                    userServiceProfileEntity.setProfileType(FilenameUtils.getBaseName(file.getName()));
                    userServiceProfileEntity.setStatus(Const.USER_PROFILE.STATUS_ON);
                    userServiceProfileEntity = repository.save(userServiceProfileEntity);
                    if (Objects.nonNull(userServiceProfileEntity)){
                        output.add(modelMapper.map(userServiceProfileEntity, UserServiceProfile.class));
                    }
                });
            }
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserServiceProfile> saveUserServiceProfile(Long userServiceId, String fileDir) {
        List<UserServiceProfile> output = new ArrayList<>();
        try {
            // insert user profile
            File[] files = FileUtils.getListFile(fileDir, "jpg", "png");
            if (files != null && files.length > 0){
                Arrays.stream(files).forEach(file -> {
                    UserServiceProfileEntity userServiceProfileEntity = new UserServiceProfileEntity();
                    userServiceProfileEntity.setUserServiceId(userServiceId);
                    userServiceProfileEntity.setProfileValue(fileDir+"/"+file.getName());
                    userServiceProfileEntity.setProfileType(FilenameUtils.getBaseName(file.getName()));
                    userServiceProfileEntity.setStatus(Const.USER_PROFILE.STATUS_ON);
                    userServiceProfileEntity = repository.save(userServiceProfileEntity);
                    if (Objects.nonNull(userServiceProfileEntity)){
                        output.add(modelMapper.map(userServiceProfileEntity, UserServiceProfile.class));
                    }
                });
            }
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
        }
        return output;
    }
}
