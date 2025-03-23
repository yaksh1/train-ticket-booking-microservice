package com.yaksh.trainms.train.util;

import com.yaksh.trainms.train.model.Train;
import com.yaksh.trainms.train.repository.TrainRepositoryV2;

import java.time.LocalDate;
import java.util.List;

/**
 * Utility interface for train service operations.
 * Provides methods to validate train details and check train existence.
 */
public interface TrainServiceUtil {

    /**
     * Validates if the train matches the given source, destination, and travel date.
     *
     * @param source      The source station of the train.
     * @param destination The destination station of the train.
     * @param travelDate  The date of travel.
     * @param train       The train object to validate.
     * @return true if the train is valid for the given criteria, false otherwise.
     */
    boolean validTrain(String source, String destination, LocalDate travelDate, Train train);

    /**
     * Checks if a train exists in the repository based on its PRN (Primary Reference Number).
     *
     * @param prn               The PRN of the train to check.
     * @param trainRepositoryV2 The repository to search for the train.
     * @return true if the train exists, false otherwise.
     */
    boolean doesTrainExist(String prn, TrainRepositoryV2 trainRepositoryV2);

}