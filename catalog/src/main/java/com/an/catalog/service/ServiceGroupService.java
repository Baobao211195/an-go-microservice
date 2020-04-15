package com.an.catalog.service;

import com.an.common.bean.Service;

import java.util.List;

public interface ServiceGroupService {

    List<Service> findServiceByServiceGroupId(Long serviceGroupId);

    Service findServiceById(Long serviceId);
}
