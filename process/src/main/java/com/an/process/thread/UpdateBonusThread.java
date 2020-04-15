package com.an.process.thread;

import com.an.common.bean.User;
import com.an.common.bean.UserInvite;
import com.an.common.bean.UserWallet;
import com.an.common.utils.Const;
import com.an.process.service.UserInviteService;
import com.an.process.service.UserService;
import com.an.process.service.UserWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class UpdateBonusThread {
    private static Logger logger = LoggerFactory.getLogger(UpdateBonusThread.class);

    @Autowired
    UserInviteService userInviteService;

    @Autowired
    UserWalletService userWalletService;

    @Autowired
    UserService userService;

    @Scheduled(cron = "0 30 0 * * ?")
    void process(){
        List<UserInvite> lst = userInviteService.findByStatus(Const.USER_INVITE.STATUS_NEW);
        if (lst != null && !lst.isEmpty()){
            logger.info("started add user bonus: " + new Date());
            lst.forEach(x->{
                // update status
                userInviteService.updateUserInviteStatus(x.getUserInviteId(), Const.USER_INVITE.STATUS_PROCESSED);

                // update user wallet
                User user = userService.findByInviteCode(x.getInviteCode());
                if (Objects.nonNull(user)) {
                    UserWallet userWallet = userWalletService.findByUserIdAndType(user.getUserId(), Const.USER_WALLET.BALANCE_TYPE_PROMOTION);
                    if (Objects.nonNull(userWallet)) {
                        try {
                            userWalletService.updateUserWallet(userWallet.getUserWalletId(), Const.USER_WALLET.OPERATE_ADDED, Const.USER_WALLET.INVITE_BONUS);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            });
            logger.info("finished add user bonus: " + new Date());
        }
    }
}
