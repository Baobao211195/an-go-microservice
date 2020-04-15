package com.an.user.service;

import com.an.common.bean.UserInvite;

import java.util.List;

public interface UserInviteService {

    List<UserInvite> findByUserId(Long userId);

    UserInvite insertUserInvite(Long userId, String inviteCode);
}
