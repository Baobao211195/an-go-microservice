package com.an.user.service;

import com.an.common.bean.UserInvite;
import com.an.common.utils.Const;
import com.an.user.entity.UserInviteEntity;
import com.an.user.repository.UserInviteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserInviteServiceImpl implements UserInviteService {

    @Autowired
    UserInviteRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserService userService;

    @Override
    public List<UserInvite> findByUserId(Long userId) {
        List<UserInvite> output = new ArrayList<>();
        List<UserInviteEntity> lst = repository.findByUserId(userId);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, UserInvite.class)));
        }
        return output;
    }

    @Override
    public UserInvite insertUserInvite(Long userId, String inviteCode) {
        UserInviteEntity entity = new UserInviteEntity();
        entity.setUserId(userId);
        entity.setInviteCode(inviteCode);
        entity.setStatus(Const.USER_INVITE.STATUS_NEW);
        entity.setInsertDatetime(userService.getSysdate());
        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, UserInvite.class);
        }
        return null;
    }
}
