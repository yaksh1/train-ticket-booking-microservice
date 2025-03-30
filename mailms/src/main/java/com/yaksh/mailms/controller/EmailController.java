package com.yaksh.mailms.controller;

import com.yaksh.mailms.DTO.ResponseDataDTO;
import com.yaksh.mailms.DTO.TicketRequestDTO;
import com.yaksh.mailms.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/email/")
public class EmailController {
    private final EmailService emailService;
    @PostMapping("sendEmail")
    public ResponseEntity<ResponseDataDTO> sendEmail(@RequestBody TicketRequestDTO ticketRequestDTO, @RequestParam String email){
        return ResponseEntity.ok(emailService.sendVerificationEmail(ticketRequestDTO,email));
    }
}
