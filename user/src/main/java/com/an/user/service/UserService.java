package com.an.user.service;

import com.an.common.bean.User;
import com.an.common.exception.LogicException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface UserService {

    public Date getSysdate();

    public User findById (Long userId);

    public User findBySocialIdAndEmailAndType(String socialId, String email, String type);

    public User findByMobileAndStatus(String mobile, String status);

    public User findByMobileAndStatusIn(String mobile, List<String> lstStatus);

    public User findByMobileAndTypeAndStatusIn(String mobile, String type, List<String> lstStatus);

    public User createUser(String fullname, String mobile, String email, String avatar, String type, String socialId, String fcmToken, String province, String inviteCode);

    public User approvedDriver (Long userId, boolean isApproved, String reason, String staffCode);

    public User updateRating (Long userId, Double rating);

    public User updateFcmToken (Long userId, String fcmToken);

    public User updateAvatarUser (Long userId, MultipartFile file);

    public User createDriver(String mobile, String email, String socialId, String fullname, String fcmToken, String province, String inviteCode, MultipartFile unzipFile, com.an.common.bean.UserService userService) throws LogicException, Exception;

    public List<User> getByProvinceAndTypeAndStatus(String province, String type, String status);
}
