package com.yaksh.userms.user.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for application-wide beans.
 * This class is used to define and configure beans that will be managed by the Spring container.
 */
@Configuration
public class AppConfig {
    // RestTemplate bean has been removed as we now use Feign clients for service communication
}