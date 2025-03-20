package com.yaksh.ticketms.ticket.service;

import com.yaksh.ticketms.ticket.DTO.ResponseDataDTO;
import com.yaksh.ticketms.ticket.model.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    ResponseDataDTO saveTicket(Ticket ticketToSave);
    ResponseDataDTO findTicketById(String idOfTicketToFind);
    ResponseDataDTO createNewTicket(Ticket ticket);

    ResponseDataDTO cancelTicket(String ticketIdToCancel);

    ResponseDataDTO rescheduleTicket(String ticketIdToReschedule,LocalDate updatedTravelDate);

    ResponseDataDTO fetchAllTickets(List<String> ticketIds);
}
