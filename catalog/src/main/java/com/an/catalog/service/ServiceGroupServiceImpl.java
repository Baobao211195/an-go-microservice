package com.an.catalog.service;

import com.an.catalog.entity.ServiceEntity;
import com.an.catalog.repository.ServiceRepository;
import com.an.common.utils.Const;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ServiceGroupServiceImpl implements ServiceGroupService {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<com.an.common.bean.Service> findServiceByServiceGroupId(Long serviceGroupId) {
        List<com.an.common.bean.Service> output = new ArrayList<>();
        List<ServiceEntity> lst = serviceRepository.findByServiceGroupIdAndStatus(serviceGroupId, Const.SERVICE.STATUS_ON);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, com.an.common.bean.Service.class)));
        }
        return output;
    }

    @Override
    public com.an.common.bean.Service findServiceById(Long serviceId) {
        Optional<ServiceEntity> optional = serviceRepository.findById(serviceId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            return modelMapper.map(optional.get(), com.an.common.bean.Service.class);
        }

        return null;
    }
}
