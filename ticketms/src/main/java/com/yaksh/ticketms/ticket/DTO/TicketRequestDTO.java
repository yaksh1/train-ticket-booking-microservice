package com.yaksh.ticketms.ticket.DTO;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) for Ticket Request.
 * This class is used to encapsulate the details of a ticket request made by a user.
 * It includes information about the train, user, travel details, and seat bookings.
 */
@Data
@Builder
public class TicketRequestDTO {

    /** The ID of the train for which the ticket is being booked. */
    private String trainId;

    /** The ID of the user making the ticket request. */
    private String userId;

    /** The email address of the user making the ticket request. */
    private String email;

    /** The source station from where the journey starts. */
    private String source;

    /** The destination station where the journey ends. */
    private String destination;

    /** The date on which the travel is scheduled. */
    private LocalDate dateOfTravel;

    /** 
     * A list of seat indices that the user intends to book.
     * Each sub-list represents a group of seats booked together.
     * For example, if a family books seats together, their indices will be in the same sub-list.
     */
    private List<List<Integer>> bookedSeatsIndex;

    /** 
     * The time at which the train is expected to arrive at the source station.
     * This helps in determining the start time of the journey.
     */
    private LocalDateTime arrivalTimeAtSource;

    /** 
     * The time at which the train is expected to reach the destination station.
     * This helps in determining the end time of the journey.
     */
    private LocalDateTime reachingTimeAtDestination;
}