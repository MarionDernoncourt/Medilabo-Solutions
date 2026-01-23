package com.medilabo.ms_notes.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Value("${medilabo.security.internal-header}")
    private String internalHeader;

    @Value("${medilabo.security.internal-secret}")
    private String internalSecret;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(internalHeader, internalSecret);
        };
    }
}
