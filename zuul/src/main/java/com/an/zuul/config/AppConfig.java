package com.an.zuul.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data @Getter @Setter
public class AppConfig {

    @Value("${http.port}")
    private int httpPort;

}
