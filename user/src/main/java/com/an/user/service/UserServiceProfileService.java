package com.an.user.service;

import com.an.common.bean.UserServiceProfile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserServiceProfileService {

    public List<UserServiceProfile> findByUserServiceId(Long userServiceId);

    public List<UserServiceProfile> uploadUserServiceProfile(Long userServiceId, String mobile, MultipartFile unzipFile);

    public List<UserServiceProfile> saveUserServiceProfile(Long userServiceId, String fileDir);

}
