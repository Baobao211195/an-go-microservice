package com.an.booking.service;

import com.an.booking.entity.DriverBusyEntity;
import com.an.booking.repository.DriverBusyRepository;
import com.an.common.bean.DriverBusy;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class DriverBusyServiceImpl implements DriverBusyService {

    private static Logger logger = LoggerFactory.getLogger(DriverBusyService.class);

    @Autowired
    DriverBusyRepository respository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public DriverBusy findById(Long id) {
        Optional<DriverBusyEntity> optional = respository.findById(id);
        if (Objects.nonNull(optional) && optional.isPresent()){
            return modelMapper.map(optional.get(), DriverBusy.class);
        }
        return null;
    }

    @Override
    public void insertDriver(Long id, Long driverId) {
        Optional<DriverBusyEntity> optional = respository.findById(id);
        if (Objects.nonNull(optional) && optional.isPresent()){
            DriverBusyEntity entity = optional.get();
            entity.getLstDriver().add(driverId);
            respository.save(entity);
        }
    }

    @Override
    public void evictDriver(Long id, Long driverId) {
        Optional<DriverBusyEntity> optional = respository.findById(id);
        if (Objects.nonNull(optional) && optional.isPresent()){
            DriverBusyEntity entity = optional.get();
            entity.getLstDriver().remove(driverId);
            respository.save(entity);
        }
    }
}
