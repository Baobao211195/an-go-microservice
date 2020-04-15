package com.an.booking.service;

import com.an.booking.entity.DriverSchedulePathEntity;
import com.an.booking.repository.DriverSchedulePathRepository;
import com.an.common.bean.DriverSchedulePath;
import com.an.common.utils.Const;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DriverSchedulePathServiceImpl implements DriverSchedulePathService {

    @Autowired
    DriverSchedulePathRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<DriverSchedulePath> findByDriverSchedulePathGroupIdAndStatus(String driverSchedulePathGroupId, String status) {
        List<DriverSchedulePath> output = new ArrayList<>();
        List<DriverSchedulePathEntity> lst = repository.findByDriverSchedulePathGroupIdAndStatus(driverSchedulePathGroupId, status);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, DriverSchedulePath.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DriverSchedulePath insertDriverSchedulePath(String driverSchedulePathGroupId, Double x, Double y, Long orderNumber) {
        DriverSchedulePathEntity entity = new DriverSchedulePathEntity();
        entity.setDriverSchedulePathGroupId(driverSchedulePathGroupId);
        entity.setX(x);
        entity.setY(y);
        entity.setOrderNumber(orderNumber);
        entity.setStatus(Const.BOOKING_PATH.STATUS_ON);

        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, DriverSchedulePath.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DriverSchedulePath> insertListDriverSchedulePath(String driverSchedulePathGroupId, List<DriverSchedulePath> lstDriverSchedulePath) {
        List<DriverSchedulePath> output = new ArrayList<>();
        if (lstDriverSchedulePath != null && !lstDriverSchedulePath.isEmpty()){
            lstDriverSchedulePath.forEach(x ->{
                output.add(insertDriverSchedulePath(driverSchedulePathGroupId, x.getX(), x.getY(), x.getOrderNumber()));
            });
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DriverSchedulePath updateDriverSchedulePath(Long driverSchedulePathId, Double x, Double y, String status) {
        Optional<DriverSchedulePathEntity> optional = repository.findById(driverSchedulePathId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            DriverSchedulePathEntity entity = optional.get();
            if (!Objects.equals(x, entity.getX())){
                entity.setX(x);
            }
            if (!Objects.equals(y, entity.getY())){
                entity.setY(y);
            }
            if (!Objects.equals(status, entity.getStatus())){
                entity.setStatus(status);
            }
            entity = repository.save(entity);
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, DriverSchedulePath.class);
            }
        }
        return null;
    }
}
