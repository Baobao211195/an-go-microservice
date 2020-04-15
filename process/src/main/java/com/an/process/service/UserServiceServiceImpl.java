package com.an.process.service;

import com.an.common.bean.UserService;
import com.an.process.entity.UserServiceEntity;
import com.an.process.repository.UserServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceServiceImpl implements UserServiceService {

    @Autowired
    UserServiceRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<UserService> findByStatusAndExpireDatetimeGreaterThan(String status, Date expireDatetime) {
        List<UserService> output = new ArrayList<>();
        List<UserServiceEntity> lst = repository.findByStatusAndExpireDatetimeGreaterThan(status, expireDatetime);
        if (Objects.nonNull(lst) && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, UserService.class)));
        }
        return output;
    }
}
