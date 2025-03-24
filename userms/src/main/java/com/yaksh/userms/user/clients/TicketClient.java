package com.yaksh.userms.user.clients;

import com.yaksh.userms.user.DTO.ResponseDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * Feign client interface to interact with the Ticket Management Service (TICKETMS).
 * Provides methods to fetch, cancel, and reschedule tickets.
 */
@FeignClient(name = "TICKETMS")
public interface TicketClient {

    /**
     * Fetches all tickets based on the provided list of ticket IDs.
     *
     * @param ticketIds List of ticket IDs to fetch.
     * @return ResponseDataDTO containing the fetched ticket details.
     */
    @GetMapping("/v1/tickets/fetchAllTickets")
    ResponseDataDTO fetchAllTickets(@RequestParam("ticketIds") List<String> ticketIds);
    
    /**
     * Fetches details of a specific ticket by its ID.
     *
     * @param ticketId The ID of the ticket to fetch.
     * @return ResponseDataDTO containing the details of the specified ticket.
     */
    @GetMapping("/v1/tickets/{ticketId}")
    ResponseDataDTO fetchTicketById(@PathVariable("ticketId") String ticketId);
    
    /**
     * Cancels a specific ticket by its ID.
     *
     * @param ticketId The ID of the ticket to cancel.
     * @return ResponseDataDTO containing the cancellation status or details.
     */
    @DeleteMapping("/v1/tickets/{ticketId}")
    ResponseDataDTO cancelTicket(@PathVariable("ticketId") String ticketId);
    
    /**
     * Reschedules a specific ticket to a new travel date.
     *
     * @param ticketId The ID of the ticket to reschedule.
     * @param updatedTravelDate The new travel date for the ticket.
     * @return ResponseDataDTO containing the rescheduling status or details.
     */
    @PutMapping("/v1/tickets/rescheduleTicket/{ticketId}")
    ResponseDataDTO rescheduleTicket(@PathVariable("ticketId") String ticketId, 
                                      @RequestParam("updatedTravelDate") LocalDate updatedTravelDate);
}