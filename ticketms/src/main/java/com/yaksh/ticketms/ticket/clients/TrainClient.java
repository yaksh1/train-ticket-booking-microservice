package com.yaksh.ticketms.ticket.clients;

import com.yaksh.ticketms.ticket.DTO.ResponseDataDTO;
import com.yaksh.ticketms.ticket.model.FreeBookedSeatsRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "TRAINMS")
public interface TrainClient {
    
    /**
     * Checks if a train can be booked for the specified parameters.
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
     */
    @PutMapping("/v1/seats/freeBookedSeats")
    ResponseDataDTO freeBookedSeats(@RequestBody FreeBookedSeatsRequestDTO requestDTO);
    
    /**
     * Books seats for a specific train and travel date.
     */
    @PostMapping("/v1/seats/bookSeats")
    ResponseDataDTO bookSeats(
            @RequestParam("trainPrn") String trainPrn,
            @RequestParam("travelDate") LocalDate travelDate,
            @RequestParam("numberOfSeatsToBeBooked") int numberOfSeatsToBeBooked
    );
}
