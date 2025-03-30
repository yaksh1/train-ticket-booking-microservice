package com.yaksh.trainms.seatManagement.service;

import com.yaksh.trainms.train.DTO.ResponseDataDTO;
import com.yaksh.trainms.train.model.Train;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for managing seat-related operations in the train management system.
 * Provides methods for checking seat availability, booking seats, freeing seats, and retrieving seat details.
 */
public interface SeatManagementService {

    /**
     * Checks if the required number of seats are available for booking on a specific train and travel date.
     *
     * @param train The train object for which seat availability is being checked.
     * @param numberOfSeatsToBeBooked The number of seats requested for booking.
     * @param travelDate The date of travel for which seat availability is being checked.
     * @return A list of lists of integers representing the available seats, if any.
     */
    List<List<Integer>> areSeatsAvailable(Train train, int numberOfSeatsToBeBooked, LocalDate travelDate);

    /**
     * Books seats on a train for a specific user and travel date.
     *
     * @param userId The ID of the user making the booking.
     * @param trainPrn The unique identifier (PRN) of the train.
     * @param source The source station for the journey.
     * @param destination The destination station for the journey.
     * @param dateOfTravel The date of travel for the booking.
     * @param numberOfSeatsToBeBooked The number of seats to be booked.
     * @param email The email address of the user for notification purposes.
     * @return A ResponseDataDTO object containing booking details and status.
     */
    ResponseDataDTO bookTrain(String userId, String trainPrn, String source, String destination, LocalDate dateOfTravel, int numberOfSeatsToBeBooked, String email);

    /**
     * Frees the booked seats on a train for a specific travel date.
     *
     * @param bookedSeats A list of lists of integers representing the seats to be freed.
     * @param trainPrn The unique identifier (PRN) of the train.
     * @param travelDate The date of travel for which the seats are to be freed.
     */
    void freeTheBookedSeats(List<List<Integer>> bookedSeats, String trainPrn, LocalDate travelDate);

    /**
     * Retrieves seat availability details for a specific train and travel date.
     *
     * @param trainPrn The unique identifier (PRN) of the train.
     * @param travelDate The date of travel for which seat details are being retrieved.
     * @return A ResponseDataDTO object containing seat availability details.
     */
    ResponseDataDTO getSeatsAtParticularDate(String trainPrn, LocalDate travelDate);

    /**
     * Books seats on a train for a specific travel date without specifying user details.
     * This method is likely used for internal or automated booking processes.
     *
     * @param trainId The unique identifier of the train.
     * @param travelDate The date of travel for which the booking is being made.
     * @param numberOfSeatsToBeBooked The number of seats to be booked.
     * @return A ResponseDataDTO object containing booking details and status.
     */
    ResponseDataDTO bookSeats(String trainId, LocalDate travelDate, int numberOfSeatsToBeBooked);

}