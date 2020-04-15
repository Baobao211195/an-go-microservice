package com.an.catalog;

import com.an.common.cors.CorsConfig;
import com.an.common.logger.SpringAOPHandle;
import com.an.common.mapper.ModelMapperConfig;
import com.an.common.swagger.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableSwagger2
@RefreshScope
@ComponentScan(basePackages = "com.an.catalog.*", basePackageClasses = {SwaggerConfig.class, CorsConfig.class, ModelMapperConfig.class, SpringAOPHandle.class})
public class CatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogApplication.class, args);
    }

}
