package com.yaksh.service_reg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Main application class for the Service Registry.
 * This application acts as a Eureka Server, which is part of Netflix OSS.
 * Eureka Server is used for service discovery in a microservices architecture.
 */
@SpringBootApplication // Marks this as a Spring Boot application.
@EnableEurekaServer // Enables the Eureka Server functionality.
public class ServiceRegApplication {

	/**
	 * The entry point of the Spring Boot application.
	 * This method initializes and runs the Service Registry application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		// Launches the Spring Boot application and starts the embedded Eureka Server.
		SpringApplication.run(ServiceRegApplication.class, args);
	}

}