package com.yaksh.ticketms.ticket.clients;

import com.yaksh.ticketms.ticket.DTO.ResponseDataDTO;
import com.yaksh.ticketms.ticket.DTO.TicketRequestDTO;
import com.yaksh.ticketms.ticket.model.Ticket;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client interface for interacting with the Email Microservice (MAILMS).
 * This interface defines the communication contract for sending emails.
 * It includes a fallback mechanism to handle failures gracefully.
 */
@FeignClient(name = "MAILMS", fallback = EmailClientFallback.class)
public interface EmailClient {

    /**
     * Sends an email using the Email Microservice.
     *
     * @param ticketRequestDTO The Ticket object containing the details for the email.
     * @param email            The recipient's email address.
     * @return ResponseDataDTO A response object containing the status and message of the email operation.
     */
    @PostMapping("/v1/email/sendEmail")
    ResponseDataDTO sendEmail(@RequestBody Ticket ticketRequestDTO, @RequestParam String email);
}

/**
 * Fallback implementation for the EmailClient interface.
 * This class provides a default response when the Email Microservice is unavailable.
 */
@Component
class EmailClientFallback implements EmailClient {

    /**
     * Fallback method for sending an email.
     * Returns a default response indicating the email service is unavailable.
     *
     * @param ticketRequestDTO The Ticket object containing the details for the email.
     * @param email            The recipient's email address.
     * @return ResponseDataDTO A response object indicating the failure of the email operation.
     */
    @Override
    public ResponseDataDTO sendEmail(Ticket ticketRequestDTO, String email) {
        // Return a failure response when the email service is unavailable
        return new ResponseDataDTO(false, "Email service unavailable, try again later", null);
    }
}