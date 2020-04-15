package com.an.process.service;

import com.an.common.bean.UserInvite;

import java.util.List;

public interface UserInviteService {

    List<UserInvite> findByStatus(String status);

    UserInvite updateUserInviteStatus(Long userInviteId, String status);
}
