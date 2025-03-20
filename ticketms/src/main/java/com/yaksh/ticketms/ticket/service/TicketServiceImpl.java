package com.yaksh.ticketms.ticket.service;

import com.yaksh.ticketms.ticket.DTO.ResponseDataDTO;
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
        // Directly delegates to the repository's findById method.
        Ticket ticketFound= ticketRepositoryV2.findById(idOfTicketToFind).orElse(null);
        if (ticketFound == null) {
            log.warn("Ticket not found: {}", idOfTicketToFind);
            throw new CustomException(String.format("Ticket ID: %s not found", idOfTicketToFind),
                    ResponseStatus.TICKET_NOT_FOUND);
        }

        return new ResponseDataDTO(true, "Ticket found", ticketFound);
    }


    @Override
    public ResponseDataDTO createNewTicket(
            Ticket ticket
    ) {
        // Generate a new ticket id
        ticket.setTicketId(UUID.randomUUID().toString());

        // Save the newly created ticket to the database and return it.
        return saveTicket(ticket);
    }

    @Override
    public ResponseDataDTO cancelTicket(String ticketIdToCancel) {
        Ticket ticketFound = ticketRepositoryV2.findById(ticketIdToCancel).orElse(null);
        if(ticketFound==null){
            throw new CustomException(String.format("Ticket ID: %s not found", ticketIdToCancel),
                    ResponseStatus.TICKET_NOT_FOUND);
        }

        // free up the seats in the train
        String url = "http://localhost:8082/v1/seats/freeBookedSeats";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        FreeBookedSeatsRequestDTO requestDTO = new FreeBookedSeatsRequestDTO(
                ticketFound.getTrainId(),
                ticketFound.getBookedSeatsIndex(),
                ticketFound.getDateOfTravel()
        );

        HttpEntity<FreeBookedSeatsRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<ResponseDataDTO> freeUpSeatsResponse = restTemplate.exchange(
                url, HttpMethod.PUT, requestEntity, ResponseDataDTO.class
        );
        // delete ticket from database
        ticketRepositoryV2.deleteById(ticketIdToCancel);

        return new ResponseDataDTO(true, String.format("Ticket ID: %s has been deleted.", ticketIdToCancel));
    }

    @Override
    public ResponseDataDTO rescheduleTicket(String ticketIdToReschedule, LocalDate updatedTravelDate) {
        // Find the ticket by its ID
        Ticket ticketFound = ticketRepositoryV2.findById(ticketIdToReschedule).orElse(null);
        if (ticketFound == null) {
            throw new CustomException(String.format("Ticket ID: %s not found", ticketIdToReschedule),
                    ResponseStatus.TICKET_NOT_FOUND);
        }

        // Check if the train can be booked for the new date
        restTemplate.exchange("http://localhost:8082/v1/train/canBeBooked?trainPrn="+ticketFound.getTrainId()
                +"&source="+ticketFound.getSource()+"&destination="+ticketFound.getDestination()
                +"&travelDate="+updatedTravelDate,HttpMethod.GET,null,ResponseDataDTO.class
        );

        // free up the seats in the train
        String url = "http://localhost:8082/v1/seats/freeBookedSeats";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        FreeBookedSeatsRequestDTO requestDTO = new FreeBookedSeatsRequestDTO(
                ticketFound.getTrainId(),
                ticketFound.getBookedSeatsIndex(),
                ticketFound.getDateOfTravel()
        );

        HttpEntity<FreeBookedSeatsRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<ResponseDataDTO> freeUpSeatsResponse = restTemplate.exchange(
                url, HttpMethod.PUT, requestEntity, ResponseDataDTO.class
        );

        // Update the ticket's travel date
        log.info("Updating the travel date in the ticket: {}", updatedTravelDate);
        ticketFound.setDateOfTravel(updatedTravelDate);

        // Save the updated ticket in the database
        log.info("Saving the ticket in the database");
        ticketRepositoryV2.save(ticketFound);

        // book seats with new date
        // Create a request body DTO
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", ticketFound.getUserId());
        requestBody.put("trainPrn", ticketFound.getTrainId());
        requestBody.put("source", ticketFound.getSource());
        requestBody.put("destination", ticketFound.getDestination());
        requestBody.put("travelDate", updatedTravelDate);
        requestBody.put("numberOfSeatsToBeBooked", ticketFound.getBookedSeatsIndex().size());

        // Create HttpEntity with body and headers
        HttpEntity<Map<String, Object>> bookingRequestEntity = new HttpEntity<>(requestBody, headers);

        // Call the API
        ResponseEntity<ResponseDataDTO> bookingResponse = restTemplate.exchange(
                "http://localhost:8082/v1/seats/book",
                HttpMethod.POST,
                bookingRequestEntity,
                ResponseDataDTO.class
        );

        return new ResponseDataDTO(true, "Travel date updated successfully");
    }

    @Override
    public ResponseDataDTO fetchAllTickets(List<String> ticketIds) {
        // Directly delegates to the repository's findById method.
        List<Ticket> tickets= ticketRepositoryV2.findAllById(ticketIds);
        return new ResponseDataDTO(true, "Ticket found", tickets);
    }


}