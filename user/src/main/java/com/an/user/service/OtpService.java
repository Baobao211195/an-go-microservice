package com.an.user.service;

import com.an.user.entity.OtpEntity;

public interface OtpService {
    OtpEntity getOtpByIsdn (String isdn);

    OtpEntity createOtp(String isdn, String otp);
}
