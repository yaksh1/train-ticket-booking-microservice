package com.yaksh.userms.user.mapper;

import com.yaksh.userms.user.DTO.UserWithTicketDTO;
import com.yaksh.userms.user.model.Ticket;
import com.yaksh.userms.user.model.User;

import java.util.List;

/**
 * Mapper class to convert a User object and a list of Ticket objects
 * into a UserWithTicketDTO object.
 */
public class UserWithTicketDTOMapper {

    /**
     * Converts a User object and a list of Ticket objects into a UserWithTicketDTO.
     *
     * @param user       The User object containing user details.
     * @param ticketList The list of Ticket objects associated with the user.
     * @return A UserWithTicketDTO object containing user details and their tickets.
     */
    public static UserWithTicketDTO convertToUserWithTicketDTO(User user, List<Ticket> ticketList) {
        // Using the builder pattern to create a UserWithTicketDTO object
        // and populating it with data from the User and Ticket objects.
        return UserWithTicketDTO.builder()
                .userId(user.getUserId()) // Setting the user ID.
                .userEmail(user.getUserEmail()) // Setting the user email.
                .hashedPassword(user.getHashedPassword()) // Setting the hashed password.
                .ticketList(ticketList) // Setting the list of tickets.
                .build(); // Building the UserWithTicketDTO object.
    }
}