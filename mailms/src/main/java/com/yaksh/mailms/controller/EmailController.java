package com.yaksh.mailms.controller;

import com.yaksh.mailms.DTO.ResponseDataDTO;
import com.yaksh.mailms.DTO.TicketRequestDTO;
import com.yaksh.mailms.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling email-related operations.
 * This class provides REST endpoints for sending emails.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/email/")
public class EmailController {

    // Dependency injection for the EmailService to handle email-related logic
    private final EmailService emailService;

    /**
     * Endpoint to send a verification email.
     *
     * @param ticketRequestDTO The details of the ticket request (request body).
     * @param email The recipient email address (request parameter).
     * @return A ResponseEntity containing a ResponseDataDTO with the operation result.
     */
    @PostMapping("sendEmail")
    public ResponseEntity<ResponseDataDTO> sendEmail(@RequestBody TicketRequestDTO ticketRequestDTO, @RequestParam String email) {
        // Delegates the email sending operation to the EmailService and wraps the result in a ResponseEntity
        return ResponseEntity.ok(emailService.sendVerificationEmail(ticketRequestDTO, email));
    }
}