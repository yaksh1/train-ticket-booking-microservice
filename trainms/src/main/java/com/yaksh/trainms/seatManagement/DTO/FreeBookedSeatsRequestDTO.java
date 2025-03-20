package com.yaksh.trainms.seatManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) for handling requests related to free and booked seats.
 * This class encapsulates the details required to manage seat bookings for a specific train and travel date.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreeBookedSeatsRequestDTO {

    /**
     * The unique identifier for the train.
     * This is used to identify the train for which the seat booking details are being requested.
     */
    private String trainPrn;

    /**
     * A list of lists representing the booked seats.
     * Each inner list contains integers representing the seat numbers that are booked.
     */
    private List<List<Integer>> bookedSeatsList;

    /**
     * The date of travel for which the seat booking details are requested.
     */
    private LocalDate travelDate;
}