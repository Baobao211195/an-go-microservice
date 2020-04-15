package com.an.process.service;

import com.an.common.bean.DriverSchedule;
import com.an.common.utils.Const;
import com.an.process.entity.DriverScheduleEntity;
import com.an.process.repository.DriverScheduleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DriverScheduleServiceImpl implements DriverScheduleService {

    @Autowired
    DriverScheduleRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<DriverSchedule> findByScheduleDateAndStatus() {
        List<DriverSchedule> output = new ArrayList<>();
        List<DriverScheduleEntity> lst = repository.findByScheduleDateAndStatus();
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, DriverSchedule.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DriverSchedule updateDriverScheduleStatus(Long driverScheduleId, String status){
        Optional<DriverScheduleEntity> optional = repository.findById(driverScheduleId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            DriverScheduleEntity entity = optional.get();
            entity.setStatus(status);
            entity.setUpdateDatetime(repository.getSysdate());
            entity = repository.save(entity);
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, DriverSchedule.class);
            }
        }
        return null;
    }
}
