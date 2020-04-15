package com.an.user.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter @Setter
public class AppConfig {

    @Value("${user.profile.upload.dir}")
    private String profileDir;

    @Value("${send.sms.token}")
    private String smsToken;

}
