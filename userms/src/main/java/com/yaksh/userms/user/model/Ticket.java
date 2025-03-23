package com.yaksh.userms.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a ticket entity in the ticket management system.
 * This class is mapped to the "tickets" collection in MongoDB.
 * It holds information about a train ticket, including user details,
 * travel details, and seat information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {
    /**
     * Unique identifier for the ticket.
     */
    private String ticketId;

    /**
     * ID of the user who booked the ticket.
     * This field is mandatory.
     */
    @NotNull(message = "User ID cannot be null")
    private String userId;

    /**
     * ID of the train associated with the ticket.
     * This field is mandatory.
     */
    @NotNull(message = "Train ID cannot be null")
    private String trainId;

    /**
     * Date of travel for the ticket.
     * This field is mandatory and is formatted as "yyyy-MM-dd".
     */
    @NotNull(message = "Date of travel cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfTravel;

    /**
     * Source station for the journey.
     * This field is mandatory.
     */
    @NotNull(message = "Source cannot be null")
    private String source;

    /**
     * Estimated arrival time at the source station.
     * This field is optional.
     */
    private LocalDateTime arrivalTimeAtSource;

    /**
     * Destination station for the journey.
     * This field is mandatory.
     */
    @NotNull(message = "Destination cannot be null")
    private String destination;

    /**
     * Estimated reaching time at the destination station.
     * This field is optional.
     */
    private LocalDateTime reachingTimeAtDestination;

    /**
     * List of booked seat indices for the ticket.
     * Each sublist represents a row, and the integers represent seat indices in that row.
     * This field is optional.
     */
    private List<List<Integer>> bookedSeatsIndex;
}