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
 * Data Transfer Object (DTO) for ticket booking requests.
 * This class encapsulates the details required for booking a ticket,
 * including user information, train details, travel dates, and seat booking details.
 */
@Getter
@Setter
@Builder
public class TicketRequestDTO {

    /**
     * Unique identifier of the user making the booking.
     * Must not be null.
     */
    @NotNull(message = "User ID cannot be null")
    private String userId;

    /**
     * Unique identifier of the train for which the ticket is being booked.
     * Must not be null.
     */
    @NotNull(message = "Train ID cannot be null")
    private String trainId;

    /**
     * Email address of the user making the booking.
     * Must not be null.
     */
    @NotNull(message = "Email cannot be null")
    private String email;

    /**
     * Date of travel for the booking.
     * Must not be null and should follow the format 'yyyy-MM-dd'.
     */
    @NotNull(message = "Date of travel cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfTravel;

    /**
     * Source station from where the journey starts.
     * Must not be null.
     */
    @NotNull(message = "Source cannot be null")
    private String source;

    /**
     * Time of arrival at the source station.
     * This field is optional and can be null if not provided.
     */
    private LocalDateTime arrivalTimeAtSource;

    /**
     * Destination station where the journey ends.
     * Must not be null.
     */
    @NotNull(message = "Destination cannot be null")
    private String destination;

    /**
     * Time of reaching the destination station.
     * This field is optional and can be null if not provided.
     */
    private LocalDateTime reachingTimeAtDestination;

    /**
     * List of booked seat indices for the ticket.
     * Each inner list represents a group of seats booked together.
     * This field is optional and can be null if no seats are booked yet.
     */
    private List<List<Integer>> bookedSeatsIndex;
}