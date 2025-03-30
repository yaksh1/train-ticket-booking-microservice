package com.yaksh.ticketms.ticket.clients;

import com.yaksh.ticketms.ticket.DTO.ResponseDataDTO;
import com.yaksh.ticketms.ticket.DTO.TicketRequestDTO;
import com.yaksh.ticketms.ticket.model.Ticket;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "MAILMS",fallback = EmailClientFallback.class)
public interface EmailClient {
    @PostMapping("/v1/email/sendEmail")
    ResponseDataDTO sendEmail(@RequestBody Ticket ticketRequestDTO, @RequestParam String email);
}
@Component
class EmailClientFallback implements EmailClient {
    @Override
    public ResponseDataDTO sendEmail(Ticket ticketRequestDTO, String email) {
        return new ResponseDataDTO(false, "Email service unavailable, try again later", null);
    }
}
