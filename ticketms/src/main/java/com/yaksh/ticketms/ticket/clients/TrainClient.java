package com.yaksh.ticketms.ticket.clients;

import com.yaksh.ticketms.ticket.DTO.ResponseDataDTO;
import com.yaksh.ticketms.ticket.model.FreeBookedSeatsRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Feign client interface for interacting with the Train Management Service (TRAINMS).
 * Provides methods for checking train availability, freeing booked seats, and booking seats.
 */
@FeignClient(name = "TRAINMS")
public interface TrainClient {
    
    /**
     * Checks if a train can be booked for the specified parameters.
     *
     * @param trainPrn     The unique identifier (PRN) of the train.
     * @param source       The source station of the journey.
     * @param destination  The destination station of the journey.
     * @param travelDate   The date of travel.
     * @return ResponseDataDTO containing the result of the check.
     */
    @GetMapping("/v1/train/canBeBooked")
    ResponseDataDTO canTrainBeBooked(
            @RequestParam("trainPrn") String trainPrn,
            @RequestParam("source") String source,
            @RequestParam("destination") String destination,
            @RequestParam("travelDate") LocalDate travelDate
    );
    
    /**
     * Frees up previously booked seats.
     *
     * @param requestDTO   The request payload containing details of the seats to be freed.
     * @return ResponseDataDTO containing the result of the operation.
     */
    @PutMapping("/v1/seats/freeBookedSeats")
    ResponseDataDTO freeBookedSeats(@RequestBody FreeBookedSeatsRequestDTO requestDTO);
    
    /**
     * Books seats for a specific train and travel date.
     *
     * @param trainPrn                  The unique identifier (PRN) of the train.
     * @param travelDate                The date of travel.
     * @param numberOfSeatsToBeBooked   The number of seats to be booked.
     * @return ResponseDataDTO containing the result of the booking operation.
     */
    @PostMapping("/v1/seats/bookSeats")
    ResponseDataDTO bookSeats(
            @RequestParam("trainPrn") String trainPrn,
            @RequestParam("travelDate") LocalDate travelDate,
            @RequestParam("numberOfSeatsToBeBooked") int numberOfSeatsToBeBooked
    );
}