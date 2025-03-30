package com.yaksh.ticketms.ticket.service;

import com.yaksh.ticketms.ticket.DTO.ResponseDataDTO;
import com.yaksh.ticketms.ticket.DTO.TicketRequestDTO;
import com.yaksh.ticketms.ticket.clients.EmailClient;
import com.yaksh.ticketms.ticket.clients.TrainClient;
import com.yaksh.ticketms.ticket.enums.ResponseStatus;
import com.yaksh.ticketms.ticket.exceptions.CustomException;
import com.yaksh.ticketms.ticket.model.FreeBookedSeatsRequestDTO;
import com.yaksh.ticketms.ticket.model.Ticket;
import com.yaksh.ticketms.ticket.repository.TicketRepositoryV2;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
    private final TrainClient trainClient;
    private final EmailClient emailClient;

    /**
     * Saves a ticket to the database.
     * 
     * @param ticketToSave The ticket object to be saved.
     * @param email The email address to send notifications.
     * @return The saved ticket object if successful, or null if an error occurs.
     */
    @Override
    @CircuitBreaker(name = "saveTicketBreaker", fallbackMethod = "saveTicketFallback")
    @Retry(name = "saveTicketRetry", fallbackMethod = "saveTicketFallback")
    public ResponseDataDTO saveTicket(Ticket ticketToSave, String email) {
        try {
            // Attempt to save the ticket to the database.
            Ticket ticket = ticketRepositoryV2.save(ticketToSave);

            // Log success if the ticket is saved successfully.
            log.info("Ticket saved successfully with id: {}", ticket.getTicketId());

            // Send email notification using the EmailClient.
            ResponseDataDTO responseDataDTO = emailClient.sendEmail(ticketToSave, email);
            log.info(responseDataDTO.toString());
            return new ResponseDataDTO(true, "Ticket saved in the DB: " + ticket.getTicketId(), ticket.getTicketId());
        } catch (Exception e) {
            // Log any exceptions that occur during the save operation.
            log.error("Error while saving ticket: {}", e.getMessage());
            throw new CustomException("Error while saving ticket: " + e.getMessage(),
                    ResponseStatus.TICKET_NOT_SAVED_IN_COLLECTION);
        }
    }

    /**
     * Fallback method for saveTicket in case of failure.
     * 
     * @param ticketToSave The ticket object that failed to save.
     * @param e The exception that triggered the fallback.
     * @return A failure response.
     */
    public ResponseDataDTO saveTicketFallback(Ticket ticketToSave, Exception e) {
        log.error("Save ticket fallback triggered due to: {}", e.getMessage());
        return new ResponseDataDTO(false, "Failed to save ticket. Please try again later.");
    }

    /**
     * Finds a ticket by its unique ID.
     * 
     * @param idOfTicketToFind The unique ID of the ticket to find.
     * @return A ResponseDataDTO containing the ticket if found, or an error response if not found.
     */
    @Override
    @CircuitBreaker(name = "findTicketBreaker", fallbackMethod = "findTicketFallback")
    @Retry(name = "findTicketRetry", fallbackMethod = "findTicketFallback")
    public ResponseDataDTO findTicketById(String idOfTicketToFind) {
        // Attempt to find the ticket by its ID.
        Ticket ticketFound = ticketRepositoryV2.findById(idOfTicketToFind).orElse(null);
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
     * Fallback method for findTicketById in case of failure.
     * 
     * @param idOfTicketToFind The ticket ID that was not found.
     * @param e The exception that triggered the fallback.
     * @return A failure response.
     */
    public ResponseDataDTO findTicketFallback(String idOfTicketToFind, Exception e) {
        log.error("Find ticket fallback triggered due to: {}", e.getMessage());
        return new ResponseDataDTO(false, "Failed to find ticket. Please try again later.");
    }

    /**
     * Creates a new ticket by assigning a unique ID and saving it to the database.
     * 
     * @param ticketRequest The ticket request object containing ticket details.
     * @return The response containing the saved ticket details.
     */
    @Override
    @CircuitBreaker(name = "createTicketBreaker", fallbackMethod = "createTicketFallback")
    @Retry(name = "createTicketRetry", fallbackMethod = "createTicketFallback")
    public ResponseDataDTO createNewTicket(TicketRequestDTO ticketRequest) {
        // Build a new Ticket object using the details from the request DTO.
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
        return saveTicket(ticket, ticketRequest.getEmail());
    }

    /**
     * Fallback method for createNewTicket in case of failure.
     * 
     * @param ticketRequest The ticket request that failed to process.
     * @param e The exception that triggered the fallback.
     * @return A failure response.
     */
    public ResponseDataDTO createTicketFallback(TicketRequestDTO ticketRequest, Exception e) {
        log.error("Create ticket fallback triggered due to: {}", e.getMessage());
        return new ResponseDataDTO(false, "Failed to create ticket. Please try again later.");
    }

    /**
     * Cancels a ticket by freeing up booked seats and deleting the ticket from the database.
     * 
     * @param ticketIdToCancel The unique ID of the ticket to cancel.
     * @return The response indicating the ticket cancellation status.
     */
    @Override
    @CircuitBreaker(name = "cancelTicketBreaker", fallbackMethod = "cancelTicketFallback")
    @Retry(name = "cancelTicketRetry", fallbackMethod = "cancelTicketFallback")
    public ResponseDataDTO cancelTicket(String ticketIdToCancel) {
        // Attempt to find the ticket by its ID.
        Ticket ticketFound = ticketRepositoryV2.findById(ticketIdToCancel).orElse(null);
        if (ticketFound == null) {
            // Throw an exception if the ticket is not found.
            throw new CustomException(String.format("Ticket ID: %s not found", ticketIdToCancel),
                    ResponseStatus.TICKET_NOT_FOUND);
        }

        // Free up the seats in the train by calling the Feign client.
        FreeBookedSeatsRequestDTO requestDTO = new FreeBookedSeatsRequestDTO(
                ticketFound.getTrainId(),
                ticketFound.getBookedSeatsIndex(),
                ticketFound.getDateOfTravel()
        );

        trainClient.freeBookedSeats(requestDTO);

        // Delete the ticket from the database.
        ticketRepositoryV2.deleteById(ticketIdToCancel);

        // Return a response indicating the ticket has been deleted.
        return new ResponseDataDTO(true, String.format("Ticket ID: %s has been deleted.", ticketIdToCancel));
    }

    /**
     * Fallback method for cancelTicket in case of failure.
     * 
     * @param ticketIdToCancel The ticket ID that failed to cancel.
     * @param e The exception that triggered the fallback.
     * @return A failure response.
     */
    public ResponseDataDTO cancelTicketFallback(String ticketIdToCancel, Exception e) {
        log.error("Cancel ticket fallback triggered due to: {}", e.getMessage());
        return new ResponseDataDTO(false, "Failed to cancel ticket. Please try again later.");
    }

    /**
     * Reschedules a ticket to a new travel date by updating the database and external services.
     * 
     * @param ticketIdToReschedule The unique ID of the ticket to reschedule.
     * @param updatedTravelDate The new travel date.
     * @return The response indicating the rescheduling status.
     */
    @Override
    @CircuitBreaker(name = "rescheduleTicketBreaker", fallbackMethod = "rescheduleTicketFallback")
    @Retry(name = "rescheduleTicketRetry", fallbackMethod = "rescheduleTicketFallback")
    public ResponseDataDTO rescheduleTicket(String ticketIdToReschedule, LocalDate updatedTravelDate) {
        // Find the ticket by its ID.
        Ticket ticketFound = ticketRepositoryV2.findById(ticketIdToReschedule).orElse(null);
        if (ticketFound == null) {
            // Throw an exception if the ticket is not found.
            throw new CustomException(String.format("Ticket ID: %s not found", ticketIdToReschedule),
                    ResponseStatus.TICKET_NOT_FOUND);
        }

        // Check if the train can be booked for the new date using the Feign client.
        trainClient.canTrainBeBooked(
                ticketFound.getTrainId(),
                ticketFound.getSource(),
                ticketFound.getDestination(),
                updatedTravelDate
        );

        // Free up the seats in the train for the old date using the Feign client.
        FreeBookedSeatsRequestDTO requestDTO = new FreeBookedSeatsRequestDTO(
                ticketFound.getTrainId(),
                ticketFound.getBookedSeatsIndex(),
                ticketFound.getDateOfTravel()
        );

        trainClient.freeBookedSeats(requestDTO);

        // Book seats for the new date using the Feign client.
        ResponseDataDTO bookingResponse = trainClient.bookSeats(
                ticketFound.getTrainId(),
                updatedTravelDate,
                ticketFound.getBookedSeatsIndex().size()
        );

        List<List<Integer>> newBookedSeatsList = (List<List<Integer>>) bookingResponse.getData();

        // Update the ticket's travel date.
        log.info("Updating the travel date in the ticket: {}", updatedTravelDate);
        ticketFound.setDateOfTravel(updatedTravelDate);
        // Update the ticket's booked seats.
        log.info("Updating the ticket's booked seats: {}", newBookedSeatsList);
        ticketFound.setBookedSeatsIndex(newBookedSeatsList);

        // Save the updated ticket in the database.
        log.info("Saving the ticket in the database");
        ticketRepositoryV2.save(ticketFound);

        // Return a response indicating the travel date has been updated successfully.
        return new ResponseDataDTO(true, "Travel date updated successfully");
    }

    /**
     * Fallback method for rescheduleTicket in case of failure.
     * 
     * @param ticketIdToReschedule The ticket ID that failed to reschedule.
     * @param updatedTravelDate The new travel date.
     * @param e The exception that triggered the fallback.
     * @return A failure response.
     */
    public ResponseDataDTO rescheduleTicketFallback(String ticketIdToReschedule, LocalDate updatedTravelDate, Exception e) {
        log.error("Reschedule ticket fallback triggered due to: {}", e.getMessage());
        return new ResponseDataDTO(false, "Failed to reschedule ticket. Please try again later.");
    }

    /**
     * Fetches all tickets based on a list of ticket IDs.
     * 
     * @param ticketIds The list of ticket IDs to fetch.
     * @return The response containing the list of fetched tickets.
     */
    @Override
    @CircuitBreaker(name = "fetchTicketsBreaker", fallbackMethod = "fetchTicketsFallback")
    @Retry(name = "fetchTicketsRetry", fallbackMethod = "fetchTicketsFallback")
    public ResponseDataDTO fetchAllTickets(List<String> ticketIds) {
        // Retrieve all tickets matching the provided IDs from the database.
        List<Ticket> tickets = ticketRepositoryV2.findAllById(ticketIds);

        // Return the found tickets.
        return new ResponseDataDTO(true, "Ticket found", tickets);
    }

    /**
     * Fallback method for fetchAllTickets in case of failure.
     * 
     * @param ticketIds The list of ticket IDs that failed to fetch.
     * @param e The exception that triggered the fallback.
     * @return A failure response.
     */
    public ResponseDataDTO fetchTicketsFallback(List<String> ticketIds, Exception e) {
        log.error("Fetch tickets fallback triggered due to: {}", e.getMessage());
        return new ResponseDataDTO(false, "Failed to fetch tickets. Please try again later.");
    }
}