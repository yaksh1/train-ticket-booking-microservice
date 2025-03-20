package com.yaksh.trainms.train.service;


import com.yaksh.trainms.train.DTO.ResponseDataDTO;
import com.yaksh.trainms.train.model.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainService {
    ResponseDataDTO searchTrains(String source, String destination, LocalDate travelDate);
    Optional<Train> findTrainByPrn(String prn);

    ResponseDataDTO addTrain(Train newTrain);
    ResponseDataDTO addMultipleTrains(List<Train> newTrains);

    ResponseDataDTO updateTrain(Train updatedTrain);

    LocalDateTime getArrivalAtSourceTime(Train train,String source,LocalDate travelDate);
    ResponseDataDTO getTrainSchedule(String trainId, LocalDate travelDate);
    Train canBeBooked(String trainPrn,String source,String destination,LocalDate travelDate);




}
