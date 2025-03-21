package com.yaksh.trainms.train.service;

import com.yaksh.trainms.train.DTO.ResponseDataDTO;
import com.yaksh.trainms.train.model.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface representing the service layer for train-related operations.
 * Provides methods to search, add, update, and retrieve train information.
 */
public interface TrainService {

    /**
     * Searches for trains based on the source, destination, and travel date.
     *
     * @param source      The starting location of the train.
     * @param destination The ending location of the train.
     * @param travelDate  The date of travel.
     * @return A ResponseDataDTO containing the search results.
     */
    ResponseDataDTO searchTrains(String source, String destination, LocalDate travelDate);

    /**
     * Finds a train by its PRN (Passenger Reservation Number).
     *
     * @param prn The PRN of the train to be searched.
     * @return An Optional containing the train if found, or empty if not found.
     */
    Train findTrainByPrn(String prn);

    /**
     * Adds a new train to the system.
     *
     * @param newTrain The train object to be added.
     * @return A ResponseDataDTO containing the result of the operation.
     */
    ResponseDataDTO addTrain(Train newTrain);

    /**
     * Adds multiple trains to the system.
     *
     * @param newTrains A list of train objects to be added.
     * @return A ResponseDataDTO containing the result of the operation.
     */
    ResponseDataDTO addMultipleTrains(List<Train> newTrains);

    /**
     * Updates the details of an existing train.
     *
     * @param updatedTrain The train object with updated details.
     * @return A ResponseDataDTO containing the result of the update operation.
     */
    ResponseDataDTO updateTrain(Train updatedTrain);

    /**
     * Calculates the arrival time of a train at its source station on a specific travel date.
     *
     * @param train      The train object.
     * @param source     The source station of the train.
     * @param travelDate The date of travel.
     * @return The arrival time at the source station as a LocalDateTime object.
     */
    LocalDateTime getArrivalAtSourceTime(Train train, String source, LocalDate travelDate);

    /**
     * Retrieves the schedule of a train for a specific travel date.
     *
     * @param trainId    The unique identifier of the train.
     * @param travelDate The date of travel.
     * @return A ResponseDataDTO containing the train schedule.
     */
    ResponseDataDTO getTrainSchedule(String trainId, LocalDate travelDate);

    /**
     * Checks if a train can be booked for a given PRN, source, destination, and travel date.
     *
     * @param trainPrn   The PRN of the train.
     * @param source     The starting location of the train.
     * @param destination The ending location of the train.
     * @param travelDate The date of travel.
     * @return The train object if it can be booked, otherwise null.
     */
    Train canBeBooked(String trainPrn, String source, String destination, LocalDate travelDate);
}