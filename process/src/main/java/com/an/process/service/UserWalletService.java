package com.an.process.service;

import com.an.common.bean.UserWallet;
import com.an.common.exception.LogicException;

public interface UserWalletService {

    public UserWallet findByUserIdAndType(Long userId, String type);

    public UserWallet updateUserWallet(Long userWalletId, String operate, Double balance) throws LogicException, Exception;

}
