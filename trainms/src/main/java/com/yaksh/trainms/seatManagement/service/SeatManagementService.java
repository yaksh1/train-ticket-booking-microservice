package com.yaksh.trainms.seatManagement.service;


import com.yaksh.trainms.train.DTO.ResponseDataDTO;
import com.yaksh.trainms.train.model.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatManagementService {
    List<List<Integer>> areSeatsAvailable(Train train, int numberOfSeatsToBeBooked, LocalDate travelDate);

    ResponseDataDTO bookTrain(String userId,String trainPrn,String source,String destination,LocalDate dateOfTravel,int numberOfSeatsToBeBooked);
    void freeTheBookedSeats(List<List<Integer>> bookedSeats, String trainPrn,LocalDate travelDate);
    ResponseDataDTO getSeatsAtParticularDate(String trainPrn, LocalDate travelDate);

}
