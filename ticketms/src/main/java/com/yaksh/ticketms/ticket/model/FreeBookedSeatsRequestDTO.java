package com.yaksh.ticketms.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) for handling requests to free booked seats.
 * This class is used to encapsulate the necessary details for processing
 * the release of booked seats for a specific train on a given travel date.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreeBookedSeatsRequestDTO {

    /**
     * The unique identifier for the train (Train PRN).
     * Used to identify the specific train for which the booked seats need to be freed.
     */
    private String trainPrn;

    /**
     * A list of lists representing the booked seats to be freed.
     * Each inner list contains integers that represent seat numbers.
     */
    private List<List<Integer>> bookedSeatsList;

    /**
     * The date of travel for which the booked seats are being freed.
     * This ensures the operation is performed for the correct travel schedule.
     */
    private LocalDate travelDate;
}