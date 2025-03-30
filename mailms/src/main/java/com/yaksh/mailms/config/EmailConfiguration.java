package com.yaksh.mailms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration class for setting up email functionality using Spring's JavaMailSender.
 * This class reads email properties from the application configuration and configures
 * a JavaMailSender bean for sending emails.
 */
@Configuration
public class EmailConfiguration {

    // The email address to be used for sending emails, injected from application properties.
    @Value("${spring.mail.username}")
    private String email;

    // The password for the email account, injected from application properties.
    @Value("${spring.mail.password}")
    private String password;

    /**
     * Configures and returns a JavaMailSender bean for sending emails.
     * The method sets up the email server properties, authentication details, and other
     * necessary configurations for using Gmail's SMTP server.
     *
     * @return a configured JavaMailSender instance.
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Setting the host for the SMTP server (Gmail in this case).
        mailSender.setHost("smtp.gmail.com");

        // Setting the email account credentials (username and password).
        mailSender.setPassword(password);
        mailSender.setUsername(email);

        // Setting the port number for the SMTP server.
        mailSender.setPort(587);

        // Configuring additional email properties for the SMTP server.
        Properties properties = mailSender.getJavaMailProperties();
        // Setting the protocol to SMTP for email transport.
        properties.put("mail.transport.protocol", "smtp");
        // Enabling SMTP authentication.
        properties.put("mail.smtp.auth", "true");
        // Enabling STARTTLS for secure email communication.
        properties.put("mail.smtp.starttls.enable", "true");
        // Enabling debug mode for detailed logs (useful during development).
        properties.put("mail.debug", "true");

        // Returning the configured JavaMailSender instance.
        return mailSender;
    }
}