package com.yaksh.trainms.train.service;

import com.yaksh.trainms.train.DTO.ResponseDataDTO;
import com.yaksh.trainms.train.enums.ResponseStatus;
import com.yaksh.trainms.train.exceptions.CustomException;
import com.yaksh.trainms.train.model.StationSchedule;
import com.yaksh.trainms.train.model.Train;
import com.yaksh.trainms.train.repository.TrainRepositoryV2;
import com.yaksh.trainms.train.util.TrainServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for managing train-related operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {
    private final TrainRepositoryV2 trainRepositoryV2;
    private final TrainServiceUtil trainServiceUtil;

    /**
     * Adds a new train to the repository.
     *
     * @param newTrain The train to be added.
     * @return ResponseDataDTO containing the result of the operation.
     */
    @Override
    public ResponseDataDTO addTrain(Train newTrain) {
        log.info("Attempting to add new train: {}", newTrain.getPrn());
        try {
            // Check if a train with the same PRN already exists
            if (trainServiceUtil.doesTrainExist(newTrain.getPrn(), trainRepositoryV2)) {
                throw new CustomException(ResponseStatus.TRAIN_ALREADY_EXISTS);
            }

            // Save the new train to the repository
            trainRepositoryV2.save(newTrain);
            log.info("Train added successfully: {}", newTrain.getPrn());
            return new ResponseDataDTO(true, "Train added in the collection", newTrain);
        } catch (Exception e) {
            log.error("Error adding train {}: {}", newTrain.getPrn(), e.getMessage(), e);
            throw new CustomException("Error while saving the train: " + e.getMessage(),
                    ResponseStatus.TRAIN_NOT_SAVED_IN_COLLECTION);
        }
    }

    /**
     * Adds multiple trains to the repository.
     *
     * @param newTrains List of trains to be added.
     * @return ResponseDataDTO containing the result of the operation.
     */
    @Override
    public ResponseDataDTO addMultipleTrains(List<Train> newTrains) {
        log.info("Attempting to add {} trains", newTrains.size());
        try {
            // Find existing trains by PRN
            List<String> existingTrainPrns = newTrains.stream().filter(
                    train -> trainServiceUtil.doesTrainExist(train.getPrn(), trainRepositoryV2)
            ).map(train -> train.getPrn()).collect(Collectors.toList());

            // Filter out new trains that do not already exist
            List<Train> newTrainsToAdd = newTrains.stream()
                    .filter(train -> !trainServiceUtil.doesTrainExist(train.getPrn(), trainRepositoryV2))
                    .collect(Collectors.toList());

            // Save the new trains to the repository
            trainRepositoryV2.saveAll(newTrainsToAdd);
            log.info("Successfully added {} trains", newTrains.size());
            log.info("Successfully skipped trains with PRN {}", existingTrainPrns);
            return new ResponseDataDTO(true, "Trains added in the collection except trains with PRN: " + existingTrainPrns, newTrainsToAdd);
        } catch (Exception e) {
            log.error("Error adding multiple trains: {}", e.getMessage(), e);
            throw new CustomException("Error while saving the train: " + e.getMessage(),
                    ResponseStatus.TRAIN_NOT_SAVED_IN_COLLECTION);
        }
    }

    /**
     * Updates an existing train in the repository.
     *
     * @param updatedTrain The train with updated details.
     * @return ResponseDataDTO containing the result of the operation.
     */
    @Override
    public ResponseDataDTO updateTrain(Train updatedTrain) {
        log.info("Attempting to update train: {}", updatedTrain.getPrn());
        try {
            // Save the updated train to the repository
            trainRepositoryV2.save(updatedTrain);
            log.info("Train updated successfully: {}", updatedTrain.getPrn());
            return new ResponseDataDTO(true, "Train updated in the collection", updatedTrain);
        } catch (Exception e) {
            log.error("Error updating train {}: {}", updatedTrain.getPrn(), e.getMessage(), e);
            throw new CustomException("Error while updating train: " + e.getMessage(),ResponseStatus.TRAIN_UPDATING_FAILED);
        }
    }

    /**
     * Gets the arrival time of a train at the source station on a specific travel date.
     *
     * @param train       The train object.
     * @param source      The source station name.
     * @param travelDate  The travel date.
     * @return The arrival time at the source station, or null if not found.
     */
    @Override
    public LocalDateTime getArrivalAtSourceTime(Train train, String source, LocalDate travelDate) {
        ResponseDataDTO isScheduleAvailable = getTrainSchedule(train.getPrn(), travelDate);
        if (isScheduleAvailable.isStatus()) {
            // Find the arrival time at the source station from the schedule
            List<StationSchedule> schedules = (List<StationSchedule>) isScheduleAvailable.getData();
            return schedules.stream()
                    .filter(schedule -> schedule.getName().equalsIgnoreCase(source))
                    .map(StationSchedule::getArrivalTime)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * Retrieves the schedule of a train for a given travel date.
     *
     * @param trainPrn    The PRN of the train.
     * @param travelDate  The travel date.
     * @return ResponseDataDTO containing the train schedule.
     */
    @Override
    public ResponseDataDTO getTrainSchedule(String trainPrn, LocalDate travelDate) {
        Train train = trainRepositoryV2.findById(trainPrn).orElse(null);
        if (train == null) {
            throw new CustomException("Train does not exist with PRN: " + trainPrn, ResponseStatus.TRAIN_NOT_FOUND);

        }
        return new ResponseDataDTO(true, String.format("Schedule of train %s fetched successfully", trainPrn), train.getSchedules().get(travelDate.toString()));
    }

    /**
     * Searches for trains between a source and destination on a specific travel date.
     *
     * @param source      The source station name.
     * @param destination The destination station name.
     * @param travelDate  The travel date.
     * @return ResponseDataDTO containing the search results.
     */
    @Override
    public ResponseDataDTO searchTrains(String source, String destination, LocalDate travelDate) {
        log.info("Searching trains from {} to {}", source, destination);
        // Filter trains that are valid for the given source, destination, and travel date
        List<Train> trains = trainRepositoryV2.findAll()
                .stream()
                .filter(train -> trainServiceUtil.validTrain(source, destination, travelDate, train))
                .collect(Collectors.toList());

        Map<String, Object> result = Map.of(
                "totalTrains", trains.size(),
                "trainsData", trains
        );

        log.info("Found {} trains from {} to {}", trains.size(), source, destination);
        return new ResponseDataDTO(true, "Trains fetched", result);
    }

    /**
     * Finds a train by its PRN.
     *
     * @param prn The PRN of the train.
     * @return An Optional containing the train if found.
     */
    @Override
    public Train findTrainByPrn(String prn) {
        Train train =  trainRepositoryV2.findById(prn).orElse(null);
        // Train not found
        if (train == null) {
            log.warn("Train not found: {}", prn);
            throw new CustomException("Train does not exist with PRN: " + prn, ResponseStatus.TRAIN_NOT_FOUND);
        }
        return train;
    }

    /**
     * Checks if a train can be booked for a given source, destination, and travel date.
     *
     * @param trainPrn    The PRN of the train.
     * @param source      The source station name.
     * @param destination The destination station name.
     * @param travelDate  The travel date.
     * @return ResponseDataDTO containing the result of the check.
     */
    @Override
    public Train canBeBooked(String trainPrn, String source, String destination, LocalDate travelDate) {
        log.info("Checking if train can be booked: {}", trainPrn);
        // Retrieve the train by PRN
        Train train = this.findTrainByPrn(trainPrn);

        // Train not found
        if (train == null) {
            log.warn("Train not found: {}", trainPrn);
            throw new CustomException("Train does not exist with PRN: " + trainPrn, ResponseStatus.TRAIN_NOT_FOUND);
        }

        // Validate if the train aligns with the given source, destination, and travel date
        boolean validTrain = trainServiceUtil.validTrain(source, destination, travelDate, train);
        if (!validTrain) {
            throw new CustomException(
                    "Can not be Booked: Source and destination do not align with train data", ResponseStatus.INVALID_DATA);
        }
        log.info("Train {} can be booked", trainPrn);
        return train;
    }

}