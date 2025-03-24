package com.yaksh.userms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The main class for the User Management System application.
 * This class serves as the entry point for the Spring Boot application.
 */
@EnableFeignClients
@SpringBootApplication
public class UsermsApplication {

	/**
	 * The main method which serves as the entry point of the application.
	 * It uses Spring Boot's SpringApplication.run() method to launch the application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		// Launch the Spring Boot application
		SpringApplication.run(UsermsApplication.class, args);
	}

}