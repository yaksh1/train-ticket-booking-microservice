package com.yaksh.ticketms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for the Ticket Management System application.
 * This class serves as the entry point for the Spring Boot application.
 */
@SpringBootApplication
public class TicketmsApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 * 
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		// Launch the Spring Boot application using the specified class as the primary source.
		SpringApplication.run(TicketmsApplication.class, args);
	}

}