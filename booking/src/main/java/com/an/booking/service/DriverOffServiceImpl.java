package com.an.booking.service;

import com.an.booking.entity.DriverBusyEntity;
import com.an.booking.entity.DriverOffEntity;
import com.an.booking.repository.DriverBusyRepository;
import com.an.booking.repository.DriverOffRepository;
import com.an.common.bean.DriverBusy;
import com.an.common.bean.DriverOff;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class DriverOffServiceImpl implements DriverOffService {

    private static Logger logger = LoggerFactory.getLogger(DriverBusyService.class);

    @Autowired
    DriverOffRepository respository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public DriverOff findById(Long id) {
        Optional<DriverOffEntity> optional = respository.findById(id);
        if (Objects.nonNull(optional) && optional.isPresent()){
            return modelMapper.map(optional.get(), DriverOff.class);
        }
        return null;
    }

    @Override
    public void insertDriver(Long id, Long driverId) {
        Optional<DriverOffEntity> optional = respository.findById(id);
        if (Objects.nonNull(optional) && optional.isPresent()){
            DriverOffEntity entity = optional.get();
            entity.getLstDriver().add(driverId);
            respository.save(entity);
        }
    }

    @Override
    public void evictDriver(Long id, Long driverId) {
        Optional<DriverOffEntity> optional = respository.findById(id);
        if (Objects.nonNull(optional) && optional.isPresent()){
            DriverOffEntity entity = optional.get();
            entity.getLstDriver().remove(driverId);
            respository.save(entity);
        }
    }
}
