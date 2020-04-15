package com.an.process.service;

import com.an.common.bean.User;

public interface UserService {
    public User updateRating (Long userId, Double rating);

    public User findByInviteCode(String inviteCode);

    public User findByUserId(Long userId);
}
