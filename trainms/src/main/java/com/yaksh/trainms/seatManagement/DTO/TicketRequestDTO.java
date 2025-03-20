package com.yaksh.trainms.seatManagement.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for ticket booking requests.
 */
@Getter
@Setter
@Builder
public class TicketRequestDTO {

    @NotNull(message = "User ID cannot be null")
    private String userId;

    @NotNull(message = "Train ID cannot be null")
    private String trainId;

    @NotNull(message = "Date of travel cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfTravel;

    @NotNull(message = "Source cannot be null")
    private String source;

    private LocalDateTime arrivalTimeAtSource;

    @NotNull(message = "Destination cannot be null")
    private String destination;

    private LocalDateTime reachingTimeAtDestination;

    private List<List<Integer>> bookedSeatsIndex;
}
