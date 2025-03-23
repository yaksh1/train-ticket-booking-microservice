package com.yaksh.userms.user.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yaksh.userms.user.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserWithTicketDTO {
    private String userId; // Unique identifier for the user.

    private String userEmail; // Email address of the user.

    private String hashedPassword; // Hashed password for secure authentication.
    private List<Ticket> ticketList;
}
