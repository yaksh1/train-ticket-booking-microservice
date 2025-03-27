package com.yaksh.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Gateway application.
 * This class initializes and runs the Spring Boot application.
 */
@SpringBootApplication
public class GatewayApplication {

    /**
     * The main method that serves as the entry point of the application.
     * It uses SpringApplication.run to bootstrap the application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        // Bootstrapping the Spring Boot application.
        SpringApplication.run(GatewayApplication.class, args);
    }

}