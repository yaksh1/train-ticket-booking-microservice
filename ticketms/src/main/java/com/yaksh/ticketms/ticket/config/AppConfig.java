package com.yaksh.ticketms.ticket.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for application-level beans.
 * This class is responsible for defining and configuring beans used in the application.
 */
@Configuration
public class AppConfig {

    /**
     * Creates and configures a {@link RestTemplate} bean.
     * 
     * @param builder a {@link RestTemplateBuilder} used to build the RestTemplate
     * @return a fully configured {@link RestTemplate} instance
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Builds and returns a RestTemplate instance using the provided builder.
        return builder.build();
    }
}