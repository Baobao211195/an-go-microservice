package com.an.common.utils;

import com.an.common.bean.SmsResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class OtpUtils {

    private static Logger logger = LoggerFactory.getLogger(OtpUtils.class);

    private static Gson gson = new Gson();
    private static String BRANDNAME = "ANGO";
    /**
     sms_type có các giá trị như sau:
     2: tin nhắn gửi bằng đầu số ngẫu nhiên
     3: tin nhắn gửi bằng brandname
     4: tin nhắn gửi bằng brandname mặc định (Verify hoặc Notify)
     5: tin nhắn gửi bằng app android
     */
    private static int SMS_TYPE = 4;

    public static String sendOtp (String token, String to) {
        try {
            SpeedSMSAPI speedSMSAPI = new SpeedSMSAPI(token);
            String otp = RandomUtils.generateOtp(Const.OTP.LENGTH);
            String response = speedSMSAPI.sendSMS(to, "Ma OTP cua ban la " + otp, SMS_TYPE, BRANDNAME);
            SmsResponse smsResponse = gson.fromJson(response, SmsResponse.class);
            if (Objects.nonNull(smsResponse) && Const.OTP.OK.equalsIgnoreCase(smsResponse.getCode())){
                return otp;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
    }
}
