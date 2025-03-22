package com.yaksh.ticketms.ticket.service;

import com.yaksh.ticketms.ticket.DTO.ResponseDataDTO;
import com.yaksh.ticketms.ticket.DTO.TicketRequestDTO;
import com.yaksh.ticketms.ticket.enums.ResponseStatus;
import com.yaksh.ticketms.ticket.exceptions.CustomException;
import com.yaksh.ticketms.ticket.model.FreeBookedSeatsRequestDTO;
import com.yaksh.ticketms.ticket.model.Ticket;
import com.yaksh.ticketms.ticket.repository.TicketRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This service class handles all ticket-related operations, such as saving, retrieving, deleting, 
 * and creating new tickets. It acts as the business logic layer for managing train tickets.
 */
@Service
@RequiredArgsConstructor // Automatically generates a constructor for final fields.
@Slf4j // Provides logging capabilities for this class.
public class TicketServiceImpl implements TicketService {

    // Repository for accessing and manipulating ticket data in the database.
    private final TicketRepositoryV2 ticketRepositoryV2;
    private final RestTemplate restTemplate;
    @Value("${trainms.service.url}")
    private String trainServiceUrl;

    /**
     * Saves a ticket to the database.
     * 
     * @param ticketToSave The ticket object to be saved.
     * @return The saved ticket object if successful, or null if an error occurs.
     */
    @Override
    public ResponseDataDTO saveTicket(Ticket ticketToSave) {
        try {
            // Attempt to save the ticket to the database.
            Ticket ticket = ticketRepositoryV2.save(ticketToSave);

            // Log success if the ticket is saved successfully.
            log.info("Ticket saved successfully with id: {}", ticket.getTicketId());
            return new ResponseDataDTO(true,"Ticket saved in the DB: "+ ticket.getTicketId(),ticket.getTicketId());
        } catch (Exception e) {
            // Log any exceptions that occur during the save operation.
            log.error("Error while saving ticket: {}", e.getMessage());
            throw new CustomException("Error while saving ticket: "+e.getMessage(),
                    ResponseStatus.TICKET_NOT_SAVED_IN_COLLECTION);
        }
    }

    /**
     * Finds a ticket by its unique ID.
     * 
     * @param idOfTicketToFind The unique ID of the ticket to find.
     * @return An Optional containing the ticket if found, or empty if not found.
     */
    @Override
    public ResponseDataDTO findTicketById(String idOfTicketToFind) {
        // Attempt to find the ticket by its ID.
        Ticket ticketFound= ticketRepositoryV2.findById(idOfTicketToFind).orElse(null);
        if (ticketFound == null) {
            // Log a warning if the ticket is not found.
            log.warn("Ticket not found: {}", idOfTicketToFind);
            throw new CustomException(String.format("Ticket ID: %s not found", idOfTicketToFind),
                    ResponseStatus.TICKET_NOT_FOUND);
        }

        // Return the found ticket.
        return new ResponseDataDTO(true, "Ticket found", ticketFound);
    }

    /**
     * Creates a new ticket by assigning a unique ID and saving it to the database.
     * 
     * @param ticket The ticket object to create.
     * @return The response containing the saved ticket details.
     */
    @Override
    public ResponseDataDTO createNewTicket(TicketRequestDTO ticketRequest) {
        Ticket ticket = Ticket.builder()
                .trainId(ticketRequest.getTrainId())
                .userId(ticketRequest.getUserId())
                .source(ticketRequest.getSource())
                .destination(ticketRequest.getDestination())
                .dateOfTravel(ticketRequest.getDateOfTravel())
                .bookedSeatsIndex(ticketRequest.getBookedSeatsIndex())
                .arrivalTimeAtSource(ticketRequest.getArrivalTimeAtSource())
                .reachingTimeAtDestination(ticketRequest.getReachingTimeAtDestination())
                .build();
        // Generate a new unique ticket ID.
        ticket.setTicketId(UUID.randomUUID().toString());
        log.info("Creating new ticket: {}", ticket);
        // Save the newly created ticket to the database and return it.
        return saveTicket(ticket);
    }

    /**
     * Cancels a ticket by freeing up booked seats and deleting the ticket from the database.
     * 
     * @param ticketIdToCancel The unique ID of the ticket to cancel.
     * @return The response indicating the ticket cancellation status.
     */
    @Override
    public ResponseDataDTO cancelTicket(String ticketIdToCancel) {
        // Attempt to find the ticket by its ID.
        Ticket ticketFound = ticketRepositoryV2.findById(ticketIdToCancel).orElse(null);
        if(ticketFound == null){
            // Throw an exception if the ticket is not found.
            throw new CustomException(String.format("Ticket ID: %s not found", ticketIdToCancel),
                    ResponseStatus.TICKET_NOT_FOUND);
        }

        // Free up the seats in the train by calling an external service.
        String url = trainServiceUrl + "/v1/seats/freeBookedSeats";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a request body for freeing up seats.
        FreeBookedSeatsRequestDTO requestDTO = new FreeBookedSeatsRequestDTO(
                ticketFound.getTrainId(),
                ticketFound.getBookedSeatsIndex(),
                ticketFound.getDateOfTravel()
        );

        HttpEntity<FreeBookedSeatsRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<ResponseDataDTO> freeUpSeatsResponse = restTemplate.exchange(
                url, HttpMethod.PUT, requestEntity, ResponseDataDTO.class
        );

        // Delete the ticket from the database.
        ticketRepositoryV2.deleteById(ticketIdToCancel);

        // Return a response indicating the ticket has been deleted.
        return new ResponseDataDTO(true, String.format("Ticket ID: %s has been deleted.", ticketIdToCancel));
    }

    /**
     * Reschedules a ticket to a new travel date by updating the database and external services.
     * 
     * @param ticketIdToReschedule The unique ID of the ticket to reschedule.
     * @param updatedTravelDate The new travel date.
     * @return The response indicating the rescheduling status.
     */
    @Override
    public ResponseDataDTO rescheduleTicket(String ticketIdToReschedule, LocalDate updatedTravelDate) {
        // Find the ticket by its ID.
        Ticket ticketFound = ticketRepositoryV2.findById(ticketIdToReschedule).orElse(null);
        if (ticketFound == null) {
            // Throw an exception if the ticket is not found.
            throw new CustomException(String.format("Ticket ID: %s not found", ticketIdToReschedule),
                    ResponseStatus.TICKET_NOT_FOUND);
        }

        // Check if the train can be booked for the new date by calling an external service.
        restTemplate.exchange(trainServiceUrl + "/v1/train/canBeBooked?trainPrn="+ticketFound.getTrainId()
                +"&source="+ticketFound.getSource()+"&destination="+ticketFound.getDestination()
                +"&travelDate="+updatedTravelDate,HttpMethod.GET,null,ResponseDataDTO.class
        );

        // Free up the seats in the train for the old date.
        String url = trainServiceUrl + "/v1/seats/freeBookedSeats";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a request body for freeing up seats.
        FreeBookedSeatsRequestDTO requestDTO = new FreeBookedSeatsRequestDTO(
                ticketFound.getTrainId(),
                ticketFound.getBookedSeatsIndex(),
                ticketFound.getDateOfTravel()
        );

        HttpEntity<FreeBookedSeatsRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<ResponseDataDTO> freeUpSeatsResponse = restTemplate.exchange(
                url, HttpMethod.PUT, requestEntity, ResponseDataDTO.class
        );

        // book seats for new date
        ResponseEntity<ResponseDataDTO> bookingResponse = restTemplate.exchange(
                trainServiceUrl + "/v1/seats/bookSeats?trainPrn="+ticketFound.getTrainId()+"&travelDate="+updatedTravelDate
                        + "&numberOfSeatsToBeBooked="+ticketFound.getBookedSeatsIndex().size(),
                HttpMethod.POST,
                null,
                ResponseDataDTO.class
        );
        List<List<Integer>> newBookedSeatsList = (List<List<Integer>>) bookingResponse.getBody().getData();
        // Update the ticket's travel date.
        log.info("Updating the travel date in the ticket: {}", updatedTravelDate);
        ticketFound.setDateOfTravel(updatedTravelDate);
        // Update the ticket's booked seats
        log.info("Updating the ticket's booked seats: {}", newBookedSeatsList);
        ticketFound.setBookedSeatsIndex(newBookedSeatsList);

        // Save the updated ticket in the database.
        log.info("Saving the ticket in the database");
        ticketRepositoryV2.save(ticketFound);

        // Return a response indicating the travel date has been updated successfully.
        return new ResponseDataDTO(true, "Travel date updated successfully");
    }

    /**
     * Fetches all tickets based on a list of ticket IDs.
     * 
     * @param ticketIds The list of ticket IDs to fetch.
     * @return The response containing the list of fetched tickets.
     */
    @Override
    public ResponseDataDTO fetchAllTickets(List<String> ticketIds) {
        // Retrieve all tickets matching the provided IDs from the database.
        List<Ticket> tickets= ticketRepositoryV2.findAllById(ticketIds);

        // Return the found tickets.
        return new ResponseDataDTO(true, "Ticket found", tickets);
    }
}