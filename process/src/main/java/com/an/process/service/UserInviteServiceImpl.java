package com.an.process.service;

import com.an.common.bean.UserInvite;
import com.an.process.entity.UserInviteEntity;
import com.an.process.repository.UserInviteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserInviteServiceImpl implements UserInviteService {

    @Autowired
    UserInviteRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<UserInvite> findByStatus(String status) {
        List<UserInvite> output = new ArrayList<>();
        List<UserInviteEntity> lst = repository.findByStatus(status);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, UserInvite.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInvite updateUserInviteStatus(Long userInviteId, String status) {
        Optional<UserInviteEntity> optional = repository.findById(userInviteId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            UserInviteEntity entity = optional.get();
            entity.setStatus(status);
            entity = repository.save(entity);

            return modelMapper.map(entity, UserInvite.class);
        }
        return null;
    }
}
