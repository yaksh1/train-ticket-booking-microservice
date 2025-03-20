package com.yaksh.trainms.train.util;

import com.yaksh.trainms.train.model.Train;
import com.yaksh.trainms.train.repository.TrainRepositoryV2;

import java.time.LocalDate;
import java.util.List;

public interface TrainServiceUtil {
    boolean validTrain(String source, String destination, LocalDate travelDate, Train train);
    boolean doesTrainExist(String prn, TrainRepositoryV2 trainRepositoryV2);
}
