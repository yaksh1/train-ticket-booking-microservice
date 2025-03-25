package com.yaksh.config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * The ConfigServerApplication class serves as the entry point for the Spring Boot application.
 * It also enables the Spring Cloud Config Server functionality by using the @EnableConfigServer annotation.
 */
@SpringBootApplication // Marks this class as a Spring Boot application.
@EnableConfigServer // Enables the Spring Cloud Config Server functionality in this application.
public class ConfigServerApplication {

	/**
	 * The main method is the entry point of the application.
	 * It uses SpringApplication.run to bootstrap and launch the Spring application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args); // Starts the Spring Boot application.
	}

}