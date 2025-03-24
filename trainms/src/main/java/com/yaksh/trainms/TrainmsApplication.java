package com.yaksh.trainms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The TrainmsApplication class serves as the entry point for the Train Management System application.
 * It initializes and starts the Spring Boot application context.
 */
@SpringBootApplication
@EnableFeignClients
public class TrainmsApplication {

    /**
     * The main method is the entry point of the Java application.
     * It uses SpringApplication.run to bootstrap the application.
     *
     * @param args Command-line arguments passed to the application
     */
    public static void main(String[] args) {
        // Launching the Spring Boot application with the specified configuration class
        SpringApplication.run(TrainmsApplication.class, args);
    }

}