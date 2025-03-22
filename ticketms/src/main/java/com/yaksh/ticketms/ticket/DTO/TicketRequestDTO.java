package com.yaksh.ticketms.ticket.DTO;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TicketRequestDTO {
    private String trainId;
    private String userId;
    private String source;
    private String destination;
    private LocalDate dateOfTravel;
    private List<List<Integer>> bookedSeatsIndex;
    private LocalDateTime arrivalTimeAtSource;
    private LocalDateTime reachingTimeAtDestination;
} 