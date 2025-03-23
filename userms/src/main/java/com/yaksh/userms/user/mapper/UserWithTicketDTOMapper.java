package com.yaksh.userms.user.mapper;

import com.yaksh.userms.user.DTO.UserWithTicketDTO;
import com.yaksh.userms.user.model.Ticket;
import com.yaksh.userms.user.model.User;

import java.util.List;

public class UserWithTicketDTOMapper {
    public static UserWithTicketDTO convertToUserWithTicketDTO(User user, List<Ticket> ticketList){
        return UserWithTicketDTO.builder()
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .hashedPassword(user.getHashedPassword())
                .ticketList(ticketList)
                .build();
    }
}
