package com.an.catalog.service;

import com.an.catalog.entity.DailyTitleEntity;
import com.an.catalog.repository.DailyTitleRepository;
import com.an.common.bean.DailyTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyTitleServiceImpl implements DailyTitleService {

    @Autowired
    DailyTitleRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public DailyTitle findCurrentDailyTitle() {
        List<DailyTitleEntity> lst = repository.findByShowDatetime();
        if (lst != null && !lst.isEmpty()){
            return modelMapper.map(lst.get(0), DailyTitle.class);
        }
        return null;
    }
}
