package com.an.catalog.service;

import com.an.common.bean.AppConfig;

import java.util.List;

public interface AppConfigService {
    List<AppConfig> findByConfigCodeAndStatus(String configCode, String status);

    public boolean isRushHours();
}
