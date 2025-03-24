package com.yaksh.trainms.seatManagement.client;

import com.yaksh.trainms.seatManagement.DTO.TicketRequestDTO;
import com.yaksh.trainms.train.DTO.ResponseDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "TICKETMS")
public interface TicketClient {
    @PostMapping("/v1/tickets/createTicket")
    ResponseDataDTO createTicket(@RequestBody TicketRequestDTO ticketRequestDTO);
}
