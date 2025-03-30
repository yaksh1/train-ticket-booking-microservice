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

    /**
     * Endpoint to book seats on a train.
     *
     * @param requestDTO contains details of the booking request such as user ID, train PRN,
     *                   source, destination, travel date, and number of seats to be booked.
     * @return a ResponseEntity containing a ResponseDataDTO with booking details.
     */
    @PostMapping("/book")
    public ResponseEntity<ResponseDataDTO> bookTrain(@RequestBody BookTrainRequestDTO requestDTO) {
        // Delegates the booking request to the service layer with the provided details.
        return ResponseEntity.ok(seatManagementService.bookTrain(
                requestDTO.getUserId(),
                requestDTO.getTrainPrn(),
                requestDTO.getSource(),
                requestDTO.getDestination(),
                requestDTO.getTravelDate(),
                requestDTO.getNumberOfSeatsToBeBooked(),
                requestDTO.getUserEmail()
        ));
    }

    /**
     * Endpoint to free previously booked seats on a train.
     *
     * @param freeBookedSeatsRequestDTO contains details of the seats to be freed, including
     *                                  the list of booked seats, train PRN, and travel date.
     * @return a ResponseEntity containing a ResponseDataDTO indicating the success of the operation.
     */
    @PutMapping("/freeBookedSeats")
    public ResponseEntity<ResponseDataDTO> freeBookedSeats(@RequestBody FreeBookedSeatsRequestDTO freeBookedSeatsRequestDTO) {
        // Calls the service layer to free the booked seats with the provided details.
        seatManagementService.freeTheBookedSeats(freeBookedSeatsRequestDTO.getBookedSeatsList(), freeBookedSeatsRequestDTO.getTrainPrn(), freeBookedSeatsRequestDTO.getTravelDate());
        // Returns a success response after freeing the seats.
        return ResponseEntity.ok(new ResponseDataDTO(true, "Seats freed successfully", null));
    }

    /**
     * Endpoint to book a specific number of seats on a train.
     *
     * @param trainPrn the unique identifier of the train.
     * @param travelDate the date of travel for which the seats are to be booked.
     * @param numberOfSeatsToBeBooked the number of seats to book.
     * @return a ResponseEntity containing a ResponseDataDTO with booking details.
     */
    @PostMapping("/bookSeats")
    public ResponseEntity<ResponseDataDTO> bookSeats(@RequestParam String trainPrn, @RequestParam LocalDate travelDate, @RequestParam int numberOfSeatsToBeBooked) {
        // Delegates the seat booking request to the service layer with the provided details.
        return ResponseEntity.ok(seatManagementService.bookSeats(trainPrn, travelDate, numberOfSeatsToBeBooked));
    }
}