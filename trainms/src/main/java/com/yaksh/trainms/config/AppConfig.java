package com.yaksh.trainms.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for application-wide beans.
 * This class is responsible for defining and configuring beans
 * that will be used throughout the application.
 */
@Configuration
public class AppConfig {

    /**
     * Creates and configures a {@link RestTemplate} bean.
     * The {@link RestTemplate} is a synchronous client to perform HTTP requests.
     *
     * @param builder a {@link RestTemplateBuilder} used to build the RestTemplate instance.
     * @return a configured {@link RestTemplate} instance.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Build and return a RestTemplate instance using the provided builder.
        return builder.build();
    }
}