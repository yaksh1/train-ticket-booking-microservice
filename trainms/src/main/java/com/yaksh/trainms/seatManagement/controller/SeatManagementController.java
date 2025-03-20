package com.yaksh.trainms.seatManagement.controller;


import com.yaksh.trainms.seatManagement.DTO.BookTrainRequestDTO;
import com.yaksh.trainms.seatManagement.DTO.FreeBookedSeatsRequestDTO;
import com.yaksh.trainms.seatManagement.service.SeatManagementService;
import com.yaksh.trainms.train.DTO.ResponseDataDTO;
import com.yaksh.trainms.train.model.Train;
import com.yaksh.trainms.train.service.TrainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for managing seat availability and bookings in trains.
 * Provides endpoints to check availability, book, and free seats.
 */
@RestController
@RequestMapping("/v1/seats")
@Slf4j
@RequiredArgsConstructor
public class SeatManagementController {

    private final SeatManagementService seatManagementService;



    @PostMapping("/book")
    public ResponseEntity<ResponseDataDTO> bookTrain(@RequestBody BookTrainRequestDTO requestDTO) {
        return ResponseEntity.ok(seatManagementService.bookTrain(
                requestDTO.getUserId(),
                requestDTO.getTrainPrn(),
                requestDTO.getSource(),
                requestDTO.getDestination(),
                requestDTO.getTravelDate(),
                requestDTO.getNumberOfSeatsToBeBooked()
        ));
    }

    @PutMapping("/freeBookedSeats")
    public ResponseEntity<ResponseDataDTO> freeBookedSeats(@RequestBody FreeBookedSeatsRequestDTO freeBookedSeatsRequestDTO) {
        seatManagementService.freeTheBookedSeats(freeBookedSeatsRequestDTO.getBookedSeatsList(), freeBookedSeatsRequestDTO.getTrainPrn(), freeBookedSeatsRequestDTO.getTravelDate());
        return ResponseEntity.ok(new ResponseDataDTO(true, "Seats freed successfully", null));
    }
}
