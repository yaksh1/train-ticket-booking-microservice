package com.yaksh.userms.user.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yaksh.userms.user.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a user along with their associated tickets.
 * This class is used to transfer user and ticket data between different layers of the application.
 * 
 * Annotations:
 * - @Builder: Provides a builder pattern for creating instances of this class.
 * - @Data: Generates getters, setters, equals, hashCode, and toString methods.
 * - @AllArgsConstructor: Generates a constructor with arguments for all fields.
 * - @NoArgsConstructor: Generates a no-argument constructor.
 * - @JsonIgnoreProperties(ignoreUnknown = true): Ignores unknown properties during JSON deserialization.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserWithTicketDTO {
    private String userId; // Unique identifier for the user.

    private String userEmail; // Email address of the user.

    private String hashedPassword; // Hashed password for secure authentication.

    private List<Ticket> ticketList; // List of tickets associated with the user.
}