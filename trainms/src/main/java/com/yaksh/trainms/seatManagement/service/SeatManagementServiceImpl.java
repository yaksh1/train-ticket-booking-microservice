package com.yaksh.trainms.seatManagement.service;

import com.yaksh.trainms.seatManagement.DTO.TicketRequestDTO;
import com.yaksh.trainms.seatManagement.client.TicketClient;
import com.yaksh.trainms.train.DTO.ResponseDataDTO;
import com.yaksh.trainms.train.enums.ResponseStatus;
import com.yaksh.trainms.train.exceptions.CustomException;
import com.yaksh.trainms.train.model.Train;
import com.yaksh.trainms.train.service.TrainService;
import com.yaksh.trainms.train.util.TrainServiceUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.*;

/**
 * Service implementation for managing train-related seat operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SeatManagementServiceImpl implements SeatManagementService {
    private final TrainServiceUtil trainServiceUtil;
    private final TrainService trainService;
    private final TicketClient ticketClient;

    /**
     * Frees previously booked seats for a train on a specific travel date.
     *
     * @param bookedSeats List of seat positions to be freed.
     * @param trainPrn    The train PRN.
     * @param travelDate  The travel date for which seats are being freed.
     */
    @Override
    @CircuitBreaker(name = "freeSeatsBreaker", fallbackMethod = "freeSeatsFallback")
    @Retry(name = "freeSeatsRetry", fallbackMethod = "freeSeatsFallback")
    public void freeTheBookedSeats(List<List<Integer>> bookedSeats, String trainPrn, LocalDate travelDate) {
        // Retrieve the current seat layout for the specified train and date
        ResponseDataDTO seatsLayout = this.getSeatsAtParticularDate(trainPrn, travelDate);
        Train train = trainService.findTrainByPrn(trainPrn);

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
            // Update the train with the modified seat layout
            trainService.updateTrain(train);
        }
    }

    public void freeSeatsFallback(List<List<Integer>> bookedSeats, String trainPrn, LocalDate travelDate, Exception e) {
        log.error("Free seats fallback triggered due to: {}", e.getMessage());
        throw new CustomException("Failed to free seats. Please try again later.", ResponseStatus.FREE_THE_SEAT_OPERATION_FAILED);
    }

    /**
     * Books seats on a train for a specific user and travel date.
     *
     * @param userId                  The ID of the user booking the seats.
     * @param trainPrn                The PRN of the train.
     * @param source                  The source station.
     * @param destination             The destination station.
     * @param dateOfTravel            The travel date.
     * @param numberOfSeatsToBeBooked The number of seats to be booked.
     * @return ResponseDataDTO containing the booking response.
     */
    @Override
    @CircuitBreaker(name = "bookTrainBreaker", fallbackMethod = "bookTrainFallback")
    @Retry(name = "bookTrainRetry", fallbackMethod = "bookTrainFallback")
    public ResponseDataDTO bookTrain(String userId, String trainPrn, String source, String destination, LocalDate dateOfTravel, int numberOfSeatsToBeBooked,String email) {
        // Check if the train can be booked and retrieve the train object
        Train train = trainService.canBeBooked(trainPrn, source, destination, dateOfTravel);

        // Marks seats as booked (1)
        ResponseDataDTO responseDataDTO = this.bookSeats(trainPrn, dateOfTravel, numberOfSeatsToBeBooked);
        log.info("Booking seats for train {}", trainPrn);
        List<List<Integer>> availableSeatsList = (List<List<Integer>>) responseDataDTO.getData();
        log.info("Available seats: {}", availableSeatsList);
        try {
            // Create the ticket request DTO
            TicketRequestDTO ticketRequestDTO = TicketRequestDTO.builder()
                    .userId(userId)
                    .trainId(trainPrn)
                    .dateOfTravel(dateOfTravel)
                    .source(source)
                    .destination(destination)
                    .email(email)
                    .bookedSeatsIndex(availableSeatsList)
                    .arrivalTimeAtSource(trainService.getArrivalAtSourceTime(train, source, dateOfTravel))
                    .reachingTimeAtDestination(trainService.getArrivalAtSourceTime(train, destination, dateOfTravel))
                    .build();
            log.info("Ticket request DTO: {}", ticketRequestDTO.getUserId());

            // Send the ticket booking request to the external service
            ResponseDataDTO ticketBookingResponse = ticketClient.createTicket(ticketRequestDTO);
            log.info("Ticket booking response: {}", ticketBookingResponse);
            return ticketBookingResponse;
        } catch (Exception e) {
            // Ticket creation failed, so roll back the seat booking
            log.error("Ticket creation failed, rolling back seat bookings: {}", e.getMessage());

            // Free the seats that were just booked
            freeTheBookedSeats(availableSeatsList, trainPrn, dateOfTravel);

            // Propagate the exception with appropriate status
            throw new CustomException("Failed to create ticket. Seat booking has been rolled back: " + e.getMessage(),
                    ResponseStatus.TICKET_NOT_CREATED);
        }
    }

    public ResponseDataDTO bookTrainFallback(String userId, String trainPrn, String source, String destination, LocalDate dateOfTravel, int numberOfSeatsToBeBooked, Exception e) {
        log.error("Book train fallback triggered due to: {}", e.getMessage());
        return new ResponseDataDTO(false, "Train booking service is currently unavailable. Please try again later.");
    }

    /**
     * Retrieves the seat layout of a train for a specific travel date.
     *
     * @param trainPrn   The PRN of the train.
     * @param travelDate The travel date.
     * @return ResponseDataDTO containing the seat layout.
     */
    @Override
    @CircuitBreaker(name = "getSeatsBreaker", fallbackMethod = "getSeatsFallback")
    @Retry(name = "getSeatsRetry", fallbackMethod = "getSeatsFallback")
    public ResponseDataDTO getSeatsAtParticularDate(String trainPrn, LocalDate travelDate) {
        // Retrieve the train by its PRN
        Train train = trainService.findTrainByPrn(trainPrn);

        // Train not found
        if (train == null) {
            throw new CustomException("Train does not exist with PRN: " + trainPrn, ResponseStatus.TRAIN_NOT_FOUND);
        }

        // Return the seat layout for the specified travel date
        return new ResponseDataDTO(true, String.format("Seats of train %s fetched successfully", trainPrn), train.getSeats().get(travelDate.toString()));
    }

    public ResponseDataDTO getSeatsFallback(String trainPrn, LocalDate travelDate, Exception e) {
        log.error("Get seats fallback triggered due to: {}", e.getMessage());
        return new ResponseDataDTO(false, "Failed to fetch seats. Please try again later.");
    }

    /**
     * Books the specified number of seats for a train on a given travel date.
     *
     * @param trainId                The PRN of the train.
     * @param travelDate             The travel date.
     * @param numberOfSeatsToBeBooked The number of seats to be booked.
     * @return ResponseDataDTO containing the list of booked seat positions.
     */
    @Override
    @CircuitBreaker(name = "bookSeatsBreaker", fallbackMethod = "bookSeatsFallback")
    @Retry(name = "bookSeatsRetry", fallbackMethod = "bookSeatsFallback")
    public ResponseDataDTO bookSeats(String trainId, LocalDate travelDate, int numberOfSeatsToBeBooked) {
        // Retrieve the train by its PRN
        Train train = trainService.findTrainByPrn(trainId);

        // Retrieve seat availability data
        List<List<Integer>> availableSeatsList = this.areSeatsAvailable(train, numberOfSeatsToBeBooked, travelDate);

        // All seats for the specified travel date
        List<List<Integer>> allSeats = train.getSeats().get(travelDate.toString());

        // Mark each specified seat as booked (1)
        availableSeatsList.forEach(seat -> allSeats.get(seat.get(0)).set(seat.get(1), 1));

        // Update the train with the modified seat layout
        trainService.updateTrain(train);
        log.info("Updating train in the DB");
        return new ResponseDataDTO(true, "seats booked", availableSeatsList);
    }

    public ResponseDataDTO bookSeatsFallback(String trainId, LocalDate travelDate, int numberOfSeatsToBeBooked, Exception e) {
        log.error("Book seats fallback triggered due to: {}", e.getMessage());
        return new ResponseDataDTO(false, "Failed to book seats. Please try again later.");
    }

    /**
     * Checks if the requested number of seats are available for a train on a specific travel date.
     *
     * @param train                  The train object.
     * @param numberOfSeatsToBeBooked The number of seats requested.
     * @param travelDate             The travel date.
     * @return List of available seat positions.
     */
    @Override
    public List<List<Integer>> areSeatsAvailable(Train train, int numberOfSeatsToBeBooked, LocalDate travelDate) {
        log.info("Checking seat availability for train {}: {} seats requested", train.getPrn(), numberOfSeatsToBeBooked);

        // Retrieve all seats for the specified travel date
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
        log.info("Continuous seats not found for train {}", train.getPrn());
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