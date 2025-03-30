package com.yaksh.userms.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for booking a train.
 * This class is used to encapsulate the details required for booking a train.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookTrainRequestDTO {
    /**
     * The unique identifier of the user making the booking.
     */
    private String userId;

    /**
     * The unique PRN (Passenger Reservation Number) of the train to be booked.
     */
    private String trainPrn;

    /**
     * The email address of the user making the booking.
     */
    private String userEmail;

    /**
     * The source station from where the journey begins.
     */
    private String source;

    /**
     * The destination station where the journey ends.
     */
    private String destination;

    /**
     * The date on which the user plans to travel.
     * This should be a future date.
     */
    private LocalDate travelDate;

    /**
     * The number of seats the user wants to book.
     * This value should be greater than zero.
     */
    private int numberOfSeatsToBeBooked;
}