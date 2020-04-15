package com.an.user.service;

import com.an.common.bean.UserWallet;
import com.an.common.exception.LogicException;

import java.util.List;

public interface UserWalletService {

    public List<UserWallet> findByUserId (Long userId);

    public UserWallet findByUserIdAndType(Long userId, String type);

    public UserWallet initUserWallet(Long userId, String type, Double balance);

    public List<UserWallet> initWalletWhenCreateUser(Long userId);

    public UserWallet updateUserWallet(Long userWalletId, String operate, Double balance) throws LogicException, Exception;

    public void minusMoney (Long userId, Double balance) throws LogicException, Exception;

}
