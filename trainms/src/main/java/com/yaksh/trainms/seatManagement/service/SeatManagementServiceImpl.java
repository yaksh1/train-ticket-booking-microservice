package com.yaksh.trainms.seatManagement.service;


import com.yaksh.trainms.seatManagement.DTO.TicketRequestDTO;
import com.yaksh.trainms.train.DTO.ResponseDataDTO;
import com.yaksh.trainms.train.enums.ResponseStatus;
import com.yaksh.trainms.train.exceptions.CustomException;
import com.yaksh.trainms.train.model.Train;
import com.yaksh.trainms.train.service.TrainService;
import com.yaksh.trainms.train.util.TrainServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

/**
 * Service implementation for managing train-related operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SeatManagementServiceImpl implements SeatManagementService {
    private final TrainServiceUtil trainServiceUtil;
    private final TrainService trainService;
    private final RestTemplate restTemplate;
    /**
     * Frees previously booked seats for a train on a specific travel date.
     *
     * @param bookedSeats List of seat positions to be freed.
     * @param trainPrn       The train prn.
     * @param travelDate  The travel date for which seats are being freed.
     */
    @Override
    public void freeTheBookedSeats(List<List<Integer>> bookedSeats, String trainPrn, LocalDate travelDate) {
        ResponseDataDTO seatsLayout = this.getSeatsAtParticularDate(trainPrn, travelDate);
        Train train = trainService.findTrainByPrn(trainPrn).orElse(null);
        // Train not found
        if (train == null) {
            log.warn("Train not found: {}", trainPrn);
            throw new CustomException("Train does not exist with PRN: " + trainPrn, ResponseStatus.TRAIN_NOT_FOUND);
        }
        log.info("Seats layout before freeing {}", seatsLayout.getData());
        if (seatsLayout.isStatus()) {
            // Retrieve the current seat layout and mark the specified seats as free (0)
            List<List<Integer>> seatsList = (List<List<Integer>>) seatsLayout.getData();
            bookedSeats.forEach(seat -> seatsList.get(seat.get(0)).set(seat.get(1), 0));
            train.getSeats().put(travelDate.toString(), seatsList);
            log.info("Seats layout after freeing {}", train.getSeats().get(travelDate.toString()));
            // update the train
            trainService.updateTrain(train);
        }
    }




    @Override
    public ResponseDataDTO bookTrain(String userId,String trainPrn, String source, String destination, LocalDate dateOfTravel,int numberOfSeatsToBeBooked) {
        // check if can be booked and get the train
        Train train = trainService.canBeBooked(trainPrn,source,destination,dateOfTravel);
        //Retrieve seat availability data
        List<List<Integer>> availableSeatsList = this.areSeatsAvailable(train,numberOfSeatsToBeBooked,dateOfTravel);
        // All seats
        List<List<Integer>> allSeats = train.getSeats().get(dateOfTravel.toString());
        // book seats (0 -> 1)
        // Mark each specified seat as booked (1)
        availableSeatsList.forEach(seat -> allSeats.get(seat.get(0)).set(seat.get(1), 1));
        // create ticket (call ticket microservice)
        // Create the request DTO
        TicketRequestDTO ticketRequestDTO = TicketRequestDTO.builder()
                .userId(userId)
                .trainId(trainPrn)
                .dateOfTravel(dateOfTravel)
                .source(source)
                .destination(destination)
                .bookedSeatsIndex(availableSeatsList)
                .arrivalTimeAtSource(trainService.getArrivalAtSourceTime(train, source, dateOfTravel))
                .reachingTimeAtDestination(trainService.getArrivalAtSourceTime(train, destination, dateOfTravel))
                .build();

        // Create the HTTP entity with the DTO as the body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TicketRequestDTO> requestEntity = new HttpEntity<>(ticketRequestDTO, headers);

        // Send the request
        ResponseEntity<ResponseDataDTO> ticketBookingResponse = restTemplate.exchange(
                "http://localhost:8083/v1/tickets/createTicket",
                HttpMethod.POST,
                requestEntity,
                ResponseDataDTO.class
        );

        // update train
        trainService.updateTrain(train);
        log.info("Updating train in the DB");

        return ticketBookingResponse.getBody();
    }

    /**
     * Retrieves the seat layout of a train for a specific travel date.
     *
     * @param trainPrn    The PRN of the train.
     * @param travelDate  The travel date.
     * @return ResponseDataDTO containing the seat layout.
     */
    @Override
    public ResponseDataDTO getSeatsAtParticularDate(String trainPrn, LocalDate travelDate) {
        Train train = trainService.findTrainByPrn(trainPrn).orElse(null);
        if (train == null) {
            throw new CustomException("Train does not exist with PRN: " + trainPrn, ResponseStatus.TRAIN_NOT_FOUND);

        }
        return new ResponseDataDTO(true, String.format("Seats of train %s fetched successfully", trainPrn), train.getSeats().get(travelDate.toString()));
    }


    /**
     * Checks if the requested number of seats are available for a train on a specific travel date.
     *
     * @param train                  The train object.
     * @param numberOfSeatsToBeBooked The number of seats requested.
     * @param travelDate             The travel date.
     * @return ResponseDataDTO containing the seat availability result.
     */
    @Override
    public List<List<Integer>> areSeatsAvailable(Train train, int numberOfSeatsToBeBooked, LocalDate travelDate) {
        log.info("Checking seat availability for train {}: {} seats requested", train.getPrn(), numberOfSeatsToBeBooked);
        List<List<Integer>> allSeats = train.getSeats().get(travelDate.toString());
        List<List<Integer>> availableSeats = new ArrayList<>();

        int totalSeats = allSeats.size() * allSeats.get(0).size(); // Total number of seats
        int foundSeats = 0;

        // If the requested number of seats exceeds the total number of seats
        if (numberOfSeatsToBeBooked > totalSeats) {
            log.warn("Not enough seats available in train {}: requested {} seats, total seats {}", train.getPrn(), numberOfSeatsToBeBooked, totalSeats);
                    throw new CustomException("Not enough seats available", ResponseStatus.NOT_ENOUGH_SEATS);

        }

        int foundContinuousSeats = 0;
        // Try to find continuous seats first
        for (int index = 0; index < totalSeats; index++) {
            int row = index / allSeats.get(0).size(); // Row number
            int col = index % allSeats.get(0).size(); // Column number

            // Reset counter and available seats if a booked seat is found
            if (allSeats.get(row).get(col) == 1) {
                foundContinuousSeats = 0;
                availableSeats = new ArrayList<>();
            } else {
                foundContinuousSeats++;
                availableSeats.add(Arrays.asList(row, col));
            }

            // If enough continuous seats are found
            if (foundContinuousSeats == numberOfSeatsToBeBooked) {
                log.info("Found {} available continuous seats in train {}", numberOfSeatsToBeBooked, train.getPrn());
                return availableSeats;
            }
        }

        // Continuous seats not found; try to find separate seats
        log.info("Continuous seats not found", numberOfSeatsToBeBooked, train.getPrn());
        availableSeats = new ArrayList<>();

        for (int index = 0; index < totalSeats; index++) {
            int row = index / allSeats.get(0).size(); // Row number
            int col = index % allSeats.get(0).size(); // Column number

            if (allSeats.get(row).get(col) == 0) { // If seat is available
                availableSeats.add(Arrays.asList(row, col));
                foundSeats++;

                // If enough separate seats are found
                if (foundSeats == numberOfSeatsToBeBooked) {
                    log.info("Found {} available seats in train {}", numberOfSeatsToBeBooked, train.getPrn());
                    return availableSeats;
                }
            }
        }

        // Not enough seats found
        log.warn("Not enough seats available in train {}: requested {} seats, found {} seats", train.getPrn(), numberOfSeatsToBeBooked, foundSeats);
        throw new CustomException("Not enough seats available", ResponseStatus.NOT_ENOUGH_SEATS);

    }
}