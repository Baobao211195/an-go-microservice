package com.an.user.service;

import com.an.common.utils.Const;
import com.an.user.entity.OtpEntity;
import com.an.user.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService {
    @Autowired
    OtpRepository repository;

    @Override
    public OtpEntity getOtpByIsdn(String isdn) {
        Optional optional = repository.findById(isdn);
        if (optional.isPresent()){
            return (OtpEntity) optional.get();
        }
        return null;
    }

    @Override
    public OtpEntity createOtp(String isdn, String otp) {
        OtpEntity otpEntity = new OtpEntity(isdn, otp, Const.OTP.TIME_TO_LIVE);
        return repository.save(otpEntity);
    }
}
