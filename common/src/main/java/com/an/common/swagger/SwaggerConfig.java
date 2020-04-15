package com.an.common.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.any;

@Configuration
public class SwaggerConfig {

    private static final String OAUTH_NAME = "spring_oauth";
    private static final String TITLE = "API Documentation for AnGo Application";
    private static final String DESCRIPTION = "Describe api specification";
    private static final String VERSION = "1.0";
//    private static final String AUTH_SERVER = "http://localhost:8787/auth-service/oauth";
    private static final String AUTH_SERVER = "http://171.244.22.197:8787/auth-service/oauth";
    private static final String CLIENT_ID = "ango";
    private static final String CLIENT_SECRET = "$2a$10$XWGXSJWgQfkfZsTEZKf.EOzGwbB1Wrd4lrZ/HgJFF.u.KMZB95Xc."; // ango@2019


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(SwaggerDocument.class))
                .paths(any())
                .build()
                .securitySchemes(Arrays.asList(securityScheme())) // , apiKey(), apiCookieKey()
                .securityContexts(Arrays.asList(securityContext()));
    }

    private ApiInfo apiInfo() {
        return new
                ApiInfoBuilder().title(TITLE).description(DESCRIPTION).version(VERSION).build();
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    @Bean
    public SecurityScheme apiKey() {
        return new ApiKey(HttpHeaders.AUTHORIZATION, "apiKey", "header");
    }

    @Bean
    public SecurityScheme apiCookieKey() {
        return new ApiKey(HttpHeaders.COOKIE, "apiKey", "cookie");
    }

    private SecurityScheme securityScheme() {
//        GrantType grantType =
//                new AuthorizationCodeGrantBuilder()
//                        .tokenEndpoint(new TokenEndpoint(AUTH_SERVER + "/token", "oauthtoken"))
//                        .tokenRequestEndpoint(
//                                new TokenRequestEndpoint(AUTH_SERVER + "/authorize", CLIENT_ID, CLIENT_SECRET))
//                        .build();

        List<GrantType> grantTypes = new ArrayList<GrantType>();
        GrantType passwordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(AUTH_SERVER + "/token");
        grantTypes.add(passwordCredentialsGrant);


        SecurityScheme oauth =
                new OAuthBuilder()
                        .name(OAUTH_NAME)
                        .grantTypes(grantTypes)
                        .scopes(Arrays.asList(scopes()))
                        .build();
        return oauth;
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("openid", "just pure OAuth2 request")
        };
        return scopes;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(new SecurityReference(OAUTH_NAME, scopes())))
                .forPaths(any())
                .build();
    }
}
