package com.an.zuul.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Primary
@Component
@EnableAutoConfiguration
public class SwaggerCentralizeConfig implements SwaggerResourcesProvider {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerCentralizeConfig.class);

    @Autowired
    DiscoveryClient discoveryClient;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> lstServiceNotLoad = Arrays.asList("zuul-gateway", "config-server", "auth-service", "eureka");
        discoveryClient.getServices().stream().filter(service -> !lstServiceNotLoad.contains(service)).forEach(service -> {
            logger.debug("Attempting service definition refresh for Service : {} ", service);
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(service);
            if(serviceInstances == null || serviceInstances.isEmpty()){ // Should not be the case kept for failsafe
                logger.info("No instances available for service : {} ", service);
            }else{
                resources.add(swaggerResource(service, "/"+service+"/v2/api-docs", "2.0"));
                logger.info("Service Definition Context Refreshed at :  {}", LocalDateTime.now());
            }
        });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
