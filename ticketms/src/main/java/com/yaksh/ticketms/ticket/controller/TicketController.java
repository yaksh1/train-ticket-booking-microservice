package com.yaksh.ticketms.ticket.controller;

import com.yaksh.ticketms.ticket.DTO.ResponseDataDTO;
import com.yaksh.ticketms.ticket.DTO.TicketRequestDTO;
import com.yaksh.ticketms.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller class for handling ticket-related API endpoints.
 * Provides functionality for creating, retrieving, updating, and deleting tickets.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/tickets")
public class TicketController {

    // Service layer dependency to handle ticket-related operations
    private final TicketService ticketService;

    /**
     * Finds a ticket by its ID.
     * @param ticketId The ID of the ticket to retrieve.
     * @return ResponseDataDTO containing the ticket details.
     */
    @GetMapping("/{ticketId}")
    public ResponseEntity<ResponseDataDTO> findTicketById(@PathVariable String ticketId) {
        // Fetch and return the ticket details based on the provided ticket ID
        return ResponseEntity.ok(ticketService.findTicketById(ticketId));
    }

    /**
     * Fetches multiple tickets based on a list of ticket IDs.
     * @param ticketIds List of ticket IDs to retrieve.
     * @return ResponseDataDTO containing the details of the tickets.
     */
    @GetMapping("fetchAllTickets")
    public ResponseEntity<ResponseDataDTO> fetchAllTickets(@RequestParam List<String> ticketIds) {
        // Fetch and return the details of all tickets matching the provided IDs
        return ResponseEntity.ok(ticketService.fetchAllTickets(ticketIds));
    }

    /**
     * Deletes a ticket by its ID.
     * @param ticketId The ID of the ticket to delete.
     * @return ResponseDataDTO containing a success message upon deletion.
     */
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<ResponseDataDTO> cancelTicket(@PathVariable String ticketId) {
        // Cancel the ticket with the specified ID and return a success message
        return ResponseEntity.ok(ticketService.cancelTicket(ticketId));
    }

    /**
     * Creates a new ticket.
     * @param ticketRequest The ticket request object containing the details of the new ticket.
     * @return ResponseDataDTO containing the details of the newly created ticket.
     */
    @PostMapping("/createTicket")
    public ResponseEntity<ResponseDataDTO> createNewTicket(@RequestBody TicketRequestDTO ticketRequest) {
        // Log the creation of a new ticket for the specified user
        log.info("Creating new ticket for user: {}", ticketRequest.getUserId());
        // Create and return the details of the new ticket
        return ResponseEntity.ok(ticketService.createNewTicket(ticketRequest));
    }

    /**
     * Reschedules a ticket to a new travel date.
     * @param ticketId The ID of the ticket to reschedule.
     * @param updatedTravelDate The new travel date for the ticket.
     * @return ResponseDataDTO containing the updated ticket details.
     */
    @PutMapping("/rescheduleTicket/{ticketId}")
    public ResponseEntity<ResponseDataDTO> rescheduleTicket(@PathVariable String ticketId, @RequestParam LocalDate updatedTravelDate) {
        // Update the travel date of the specified ticket and return the updated ticket details
        return ResponseEntity.ok(ticketService.rescheduleTicket(ticketId, updatedTravelDate));
    }
}