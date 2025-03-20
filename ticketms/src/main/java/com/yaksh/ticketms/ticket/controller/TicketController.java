package com.yaksh.ticketms.ticket.controller;

import com.yaksh.ticketms.ticket.DTO.ResponseDataDTO;
import com.yaksh.ticketms.ticket.model.Ticket;
import com.yaksh.ticketms.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{ticketId}")
    public ResponseEntity<ResponseDataDTO> findTicketById(@PathVariable String ticketId) {
        return ResponseEntity.ok(ticketService.findTicketById(ticketId));
    }

    @GetMapping("fetchAllTickets")
    public ResponseEntity<ResponseDataDTO> fetchAllTickets(@RequestParam List<String> ticketIds) {
        return ResponseEntity.ok(ticketService.fetchAllTickets(ticketIds));
    }

    /**
     * Deletes a ticket by its ID.
     * @param ticketId The ID of the ticket to delete.
     * @return Success message upon deletion.
     */
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<ResponseDataDTO> cancelTicket(@PathVariable String ticketId) {
        return ResponseEntity.ok(ticketService.cancelTicket(ticketId));
    }

    @PostMapping("/createTicket")
    public ResponseEntity<ResponseDataDTO> createNewTicket(@RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.createNewTicket(ticket));
    }

    @PutMapping("/rescheduleTicket/{ticketId}")
    public ResponseEntity<ResponseDataDTO> rescheduleTicket(@PathVariable String ticketId,@RequestParam LocalDate updatedTravelDate){
        return ResponseEntity.ok(ticketService.rescheduleTicket(ticketId,updatedTravelDate));
    }



}
