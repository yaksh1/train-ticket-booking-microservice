package com.yaksh.mailms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The MailmsApplication class serves as the entry point for the Spring Boot application.
 * It enables the application to use Feign clients for making HTTP requests to other services.
 */
@SpringBootApplication // Indicates that this is a Spring Boot application.
@EnableFeignClients // Enables Feign clients in the application for inter-service communication.
public class MailmsApplication {

    /**
     * The main method serves as the entry point of the application.
     * It starts the Spring Boot application by invoking the run method on SpringApplication.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(MailmsApplication.class, args); // Bootstraps the application.
    }

}