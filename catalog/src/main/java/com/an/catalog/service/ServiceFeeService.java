package com.an.catalog.service;

import com.an.common.bean.ServiceFee;
import com.an.common.exception.LogicException;

import java.util.List;

public interface ServiceFeeService {
    ServiceFee findByServiceIdAndStatus(Long serviceId, String status);

    ServiceFee getTripFee(Long serviceId, Long userPromotionId, Double distance, Long minute) throws LogicException, Exception;
}
