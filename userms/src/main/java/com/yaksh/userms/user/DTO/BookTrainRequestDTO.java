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
     * The source station from where the journey begins.
     */
    private String source;

    /**
     * The destination station where the journey ends.
     */
    private String destination;

    /**
     * The date on which the user plans to travel.
     */
    private LocalDate travelDate;

    /**
     * The number of seats the user wants to book.
     */
    private int numberOfSeatsToBeBooked;
}