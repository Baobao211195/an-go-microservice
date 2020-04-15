package com.an.catalog.service;

import com.an.catalog.entity.AppConfigEntity;
import com.an.catalog.repository.AppConfigRepository;
import com.an.common.bean.AppConfig;
import com.an.common.utils.Const;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AppConfigServiceImpl implements AppConfigService {

    private Logger logger = LoggerFactory.getLogger(AppConfigService.class);

    @Autowired
    AppConfigRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Cacheable(cacheNames = "findByConfigCodeAndStatus", key = "#configCode,#status", unless = "#result == null ")
    public List<AppConfig> findByConfigCodeAndStatus(String configCode, String status) {
        List<AppConfig> output = new ArrayList<>();
        List<AppConfigEntity> lst = repository.findByConfigCodeAndStatus(configCode, status);
        if (Objects.nonNull(lst) && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, AppConfig.class)));
        }
        return output;
    }

    @Override
    public boolean isRushHours(){
        try {
            List<AppConfig> lstAppConfig = findByConfigCodeAndStatus(Const.APP_CONFIG.CONFIG_CODE_RUSH_HOUR, Const.APP_CONFIG.STATUS_ON);
            if (Objects.nonNull(lstAppConfig) && !lstAppConfig.isEmpty()) {
                LocalDateTime localDateTime = LocalDateTime.now();
                int hour = localDateTime.getHour();
                int minute = localDateTime.getMinute();
                String value;
                for (AppConfig appConfig : lstAppConfig) {
                    value = appConfig.getConfigValue();
                    String[] str = value.split("-", 2);
                    if (str != null && str.length == 2) {
                        String[] from = str[0].split(":", 2);
                        String[] to = str[1].split(":", 2);
                        if (Integer.parseInt(from[0]) < hour && hour < Integer.parseInt(to[0])){
                            return true;
                        } else {
                            if (Integer.parseInt(from[0]) == hour && Integer.parseInt(to[0]) == hour) {
                                if (Integer.parseInt(from[1]) <= minute && minute <= Integer.parseInt(to[1])){
                                    return true;
                                }
                            } else {
                                if (Integer.parseInt(from[0]) == hour && Integer.parseInt(from[1]) <= minute){
                                    return true;
                                }
                                if (Integer.parseInt(to[0]) == hour && Integer.parseInt(to[1]) >= minute){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }
}
