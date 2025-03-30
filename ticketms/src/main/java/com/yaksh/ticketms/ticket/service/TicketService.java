package com.yaksh.ticketms.ticket.service;

import com.yaksh.ticketms.ticket.DTO.ResponseDataDTO;
import com.yaksh.ticketms.ticket.DTO.TicketRequestDTO;
import com.yaksh.ticketms.ticket.model.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface representing the Ticket Service.
 * Provides methods for managing tickets such as saving, finding, creating, canceling, rescheduling, and fetching tickets.
 */
public interface TicketService {

    /**
     * Saves a ticket to the database or storage.
     * 
     * @param ticketToSave the ticket object to be saved.
     * @param email the email of the user associated with the ticket.
     * @return a ResponseDataDTO containing the result of the save operation.
     */
    ResponseDataDTO saveTicket(Ticket ticketToSave, String email);

    /**
     * Finds a ticket by its unique ID.
     * 
     * @param idOfTicketToFind the unique ID of the ticket to find.
     * @return a ResponseDataDTO containing the ticket information if found, or an appropriate message if not found.
     */
    ResponseDataDTO findTicketById(String idOfTicketToFind);

    /**
     * Creates a new ticket.
     * 
     * @param ticketRequest the ticket request object containing the details of the new ticket to create.
     * @return a ResponseDataDTO containing the result of the ticket creation operation.
     */
    ResponseDataDTO createNewTicket(TicketRequestDTO ticketRequest);

    /**
     * Cancels a ticket by its unique ID.
     * 
     * @param ticketIdToCancel the unique ID of the ticket to cancel.
     * @return a ResponseDataDTO containing the result of the cancellation operation.
     */
    ResponseDataDTO cancelTicket(String ticketIdToCancel);

    /**
     * Reschedules a ticket to a new travel date.
     * 
     * @param ticketIdToReschedule the unique ID of the ticket to reschedule.
     * @param updatedTravelDate the new travel date to update the ticket with.
     * @return a ResponseDataDTO containing the result of the rescheduling operation.
     */
    ResponseDataDTO rescheduleTicket(String ticketIdToReschedule, LocalDate updatedTravelDate);

    /**
     * Fetches all tickets by their unique IDs.
     * 
     * @param ticketIds the list of unique ticket IDs to fetch.
     * @return a ResponseDataDTO containing the list of tickets found or an appropriate message if no tickets are found.
     */
    ResponseDataDTO fetchAllTickets(List<String> ticketIds);
}