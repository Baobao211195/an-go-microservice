package com.an.security.config;

import com.an.common.bean.MsgWrapper;
import com.an.common.bean.User;
import com.an.common.utils.Const;
import com.an.security.client.UserService;
import com.an.security.user.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MsgWrapper<User> msgWrapper = userService.findByUsername(username);
        if (Const.WS.OK.equalsIgnoreCase(msgWrapper.getCode())){
            return UserPrincipal.create(msgWrapper.getWrapper());
        }
        return null;
    }
}
