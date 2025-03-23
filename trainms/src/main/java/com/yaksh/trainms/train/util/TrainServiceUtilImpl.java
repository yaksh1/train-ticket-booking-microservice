package com.yaksh.trainms.train.util;

import com.yaksh.trainms.train.model.StationSchedule;
import com.yaksh.trainms.train.model.Train;
import com.yaksh.trainms.train.repository.TrainRepositoryV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service implementation for utility methods related to Train operations.
 */
@Service
@Slf4j
public class TrainServiceUtilImpl implements TrainServiceUtil {

    /**
     * Validates if a train is valid for the given source, destination, and travel date.
     *
     * @param source      The name of the source station.
     * @param destination The name of the destination station.
     * @param travelDate  The travel date for which the train is being validated.
     * @param train       The train object containing schedule details.
     * @return true if the train is valid for the given source, destination, and travel date; false otherwise.
     */
    @Override
    public boolean validTrain(String source, String destination, LocalDate travelDate, Train train) {
        // Retrieve the schedule for the given travel date.
        List<StationSchedule> schedules = train.getSchedules().get(travelDate.toString());
        
        // If no schedule exists for the given date, return false.
        if (schedules == null) {
            return false;
        }

        // Log the retrieved schedules for debugging purposes.
        log.info(schedules.toString());

        // Find the index of the source station in the schedule.
        int sourceIndx = schedules.stream()
                .filter(station -> station.getName().equalsIgnoreCase(source)) // Match station name with source.
                .findFirst()
                .map(schedules::indexOf) // Get the index of the matched station.
                .orElse(-1); // Default to -1 if no match is found.

        // Find the index of the destination station in the schedule.
        int destinationIndx = schedules.stream()
                .filter(station -> station.getName().equalsIgnoreCase(destination)) // Match station name with destination.
                .findFirst()
                .map(schedules::indexOf) // Get the index of the matched station.
                .orElse(-1); // Default to -1 if no match is found.

        // Return true only if both source and destination are found and source comes before destination.
        return sourceIndx != -1 && destinationIndx != -1 && sourceIndx < destinationIndx;
    }

    /**
     * Checks if a train exists in the repository given its PRN (Primary Reference Number).
     *
     * @param prn               The PRN of the train to be checked.
     * @param trainRepositoryV2 The repository to search for the train.
     * @return true if the train exists in the repository; false otherwise.
     */
    @Override
    public boolean doesTrainExist(String prn, TrainRepositoryV2 trainRepositoryV2) {
        // Use the repository to check if a train with the given PRN exists.
        return trainRepositoryV2.findById(prn).isPresent();
    }
}