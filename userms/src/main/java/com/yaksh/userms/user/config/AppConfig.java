package com.yaksh.userms.user.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for application-wide beans.
 * This class is used to define and configure beans that will be managed by the Spring container.
 */
@Configuration
public class AppConfig {

    /**
     * Creates and configures a {@link RestTemplate} bean.
     * The {@link RestTemplate} is used to make HTTP requests to external services.
     *
     * @param builder the {@link RestTemplateBuilder} used to configure the {@link RestTemplate}.
     * @return a fully configured {@link RestTemplate} instance.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Build and return a new RestTemplate instance using the provided builder.
        return builder.build();
    }
}