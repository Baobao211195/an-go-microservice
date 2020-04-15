package com.an.booking;

import com.an.common.converter.StringToDriverScheduleConverter;
import com.an.common.converter.StringToDriverSchedulePathConverter;
import com.an.common.cors.CorsConfig;
import com.an.common.logger.SpringAOPHandle;
import com.an.common.mapper.ModelMapperConfig;
import com.an.common.swagger.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableSwagger2
@RefreshScope
@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrix
@ComponentScan(basePackages = "com.an.booking.*", basePackageClasses = {SwaggerConfig.class, CorsConfig.class, ModelMapperConfig.class, SpringAOPHandle.class, StringToDriverScheduleConverter.class, StringToDriverSchedulePathConverter.class})
public class BookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

}
