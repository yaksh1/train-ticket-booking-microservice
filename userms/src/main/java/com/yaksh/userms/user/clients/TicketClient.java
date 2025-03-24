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

@FeignClient(name = "TICKETMS")
public interface TicketClient {
    @GetMapping("/v1/tickets/fetchAllTickets")
    ResponseDataDTO fetchAllTickets(@RequestParam("ticketIds") List<String> ticketIds);
    
    @GetMapping("/v1/tickets/{ticketId}")
    ResponseDataDTO fetchTicketById(@PathVariable("ticketId") String ticketId);
    
    @DeleteMapping("/v1/tickets/{ticketId}")
    ResponseDataDTO cancelTicket(@PathVariable("ticketId") String ticketId);
    
    @PutMapping("/v1/tickets/rescheduleTicket/{ticketId}")
    ResponseDataDTO rescheduleTicket(@PathVariable("ticketId") String ticketId, 
                                      @RequestParam("updatedTravelDate") LocalDate updatedTravelDate);
}
