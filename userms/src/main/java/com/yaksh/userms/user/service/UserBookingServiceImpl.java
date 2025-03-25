package com.yaksh.userms.user.service;

import com.yaksh.userms.user.DTO.BookTrainRequestDTO;
import com.yaksh.userms.user.DTO.ResponseDataDTO;
import com.yaksh.userms.user.DTO.UserWithTicketDTO;
import com.yaksh.userms.user.clients.TicketClient;
import com.yaksh.userms.user.clients.TrainClient;
import com.yaksh.userms.user.enums.ResponseStatus;
import com.yaksh.userms.user.exceptions.CustomException;
import com.yaksh.userms.user.mapper.UserWithTicketDTOMapper;
import com.yaksh.userms.user.model.Ticket;
import com.yaksh.userms.user.model.User;
import com.yaksh.userms.user.repository.UserRepositoryV2;
import com.yaksh.userms.user.util.UserServiceUtil;
import com.yaksh.userms.user.util.ValidationChecks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.*;

/**
 * Service implementation for user booking-related operations.
 * Handles user login, signup, ticket booking, ticket cancellation,
 * fetching tickets, and rescheduling tickets.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserBookingServiceImpl implements UserBookingService {
    private User loggedInUser; // Currently logged-in user
    private final UserServiceUtil userServiceUtil;
    private final ValidationChecks validationChecks;
    private final UserRepositoryV2 userRepositoryV2;
    private final TicketClient ticketClient;
    private final TrainClient trainClient;

    /**
     * Sets the logged-in user.
     *
     * @param user The user object to set as logged-in.
     */
    private void setLoggedInUser(User user) {
        this.loggedInUser = user;
        log.info("User logged in as: {}", user.getUserEmail());
    }

    /**
     * Gets the currently logged-in user.
     *
     * @return The logged-in user object.
     */
    private User getLoggedInUser() {
        log.debug("Retrieving logged in user");
        return this.loggedInUser;
    }

    /**
     * Retrieves the list of all users.
     *
     * @return List of all users.
     */
    private List<User> getUserList() {
        log.info("Retrieving all users from repository");
        return userRepositoryV2.findAll();
    }

    /**
     * Logs in a user with the provided username and password.
     *
     * @param userEmail The username of the user.
     * @param password The password of the user.
     * @return ResponseDataDTO containing login result.
     */
    @Override
    public ResponseDataDTO loginUser(String userEmail, String password) {
        log.info("Login attempt for user: {}", userEmail);
        // Check if the email is valid
        if (!validationChecks.isValidEmail(userEmail.toLowerCase())) {
            log.warn("Login failed - email is not valid: {}", userEmail);
            throw new CustomException(ResponseStatus.EMAIL_NOT_VALID);
        }
        return userRepositoryV2.findByUserEmail(userEmail.toLowerCase())
                .map(user -> {
                    // Validate the provided password against the stored hashed password
                    if (!userServiceUtil.checkPassword(password, user.getHashedPassword())) {
                        throw new CustomException(ResponseStatus.PASSWORD_INCORRECT);
                    }
                    this.loggedInUser = user;
                    // Fetch all tickets associated with the user
                    List<Ticket> allTickets =(List<Ticket>) fetchAllTickets().getData();
                    // Convert user and tickets to DTO
                    UserWithTicketDTO userWithTicketDTO = UserWithTicketDTOMapper.convertToUserWithTicketDTO(user,allTickets);
                    return new ResponseDataDTO(true, "User Found", userWithTicketDTO);
                })
                .orElseThrow(() -> new CustomException(ResponseStatus.USER_NOT_FOUND));
    }

    /**
     * Signs up a new user with the provided username and password.
     *
     * @param userEmail The username of the new user.
     * @param password The password of the new user.
     * @return ResponseDataDTO containing signup result.
     */
    @Override
    public ResponseDataDTO signupUser(String userEmail, String password) {
        log.info("Signup attempt for user: {}", userEmail);
        // Check if the email is valid
        if (!validationChecks.isValidEmail(userEmail)) {
            log.warn("Signup failed - email is not valid: {}", userEmail);
            throw new CustomException(ResponseStatus.EMAIL_NOT_VALID);
        }
        // Check if the user already exists
        if (validationChecks.isUserPresent(userEmail)) {
            log.warn("Signup failed - user already exists: {}", userEmail);
            throw new CustomException(ResponseStatus.USER_ALREADY_EXISTS);
        }
        try {
            // Create a new user and hash the password
            User user = new User(UUID.randomUUID().toString(), userEmail,
                    userServiceUtil.hashPassword(password), new ArrayList<>());

            // Save the user in the repository
            User savedUser = userRepositoryV2.save(user);

            return new ResponseDataDTO(true, "User Saved in the collection", savedUser);
        } catch (Exception e) {
            log.error("Error while saving user in the collection: {}", e.getMessage(), e);
            throw new CustomException("Error while saving user in the collection: " + e.getMessage(),
                    ResponseStatus.USER_NOT_SAVED_IN_COLLECTION);
        }
    }

    /**
     * Books a ticket for the logged-in user.
     *
     * @param trainPrn              The train's PRN.
     * @param source                The source station.
     * @param destination           The destination station.
     * @param dateOfTravel          The travel date.
     * @param numberOfSeatsToBeBooked The number of seats to be booked.
     * @return ResponseDataDTO containing booking result.
     */
    @Override
    public ResponseDataDTO bookTicket(String trainPrn, String source, String destination,
                                      LocalDate dateOfTravel, int numberOfSeatsToBeBooked) {
        log.info("Booking attempt - Train: {}, Seats: {}", trainPrn, numberOfSeatsToBeBooked);

        // Ensure the user is logged in
        if (loggedInUser == null) {
            log.warn("Unauthorized booking attempt - no logged in user");
            throw new CustomException("Please log in to book the ticket", ResponseStatus.USER_NOT_FOUND);
        }

        // Ensure the travel date is not in the past
        if (dateOfTravel.isBefore(LocalDate.now())) {
            throw new CustomException("Date of travel cannot be in the past", ResponseStatus.INVALID_DATA);
        }

        //Book Train (Call Train Microservice API)
        // Create a request body DTO
        BookTrainRequestDTO bookTrainRequestDTO = BookTrainRequestDTO.builder()
                .userId(loggedInUser.getUserId())
                .trainPrn(trainPrn)
                .source(source)
                .destination(destination)
                .travelDate(dateOfTravel)
                .numberOfSeatsToBeBooked(numberOfSeatsToBeBooked)
                .build();

        ResponseDataDTO bookingResponse = trainClient.bookSeats(bookTrainRequestDTO);

        try {
            // Get the booked ticket ID from the response
            String ticketBookedId = (String) bookingResponse.getData();

            // Update the user's booked ticket list
            loggedInUser.getTicketsBookedIds().add(ticketBookedId);
            log.info("Updating logged in user ticket list");

            // Save the updated user in the user database
            userRepositoryV2.save(loggedInUser);
            log.info("Saving user in the DB");

            return new ResponseDataDTO(true, "Ticket Booked with ID: " + ticketBookedId, ticketBookedId);
        } catch (Exception e) {
            log.error("Error while booking ticket: {}", e.getMessage(), e);
            throw new CustomException("Error while booking ticket: " + e.getMessage(), ResponseStatus.TICKET_NOT_BOOKED);
        }
    }

    /**
     * Fetches all tickets booked by the logged-in user.
     *
     * @return ResponseDataDTO containing the list of tickets.
     */
    @Override
    public ResponseDataDTO fetchAllTickets() {
        log.info("Fetching all tickets for logged in user");

        // Ensure the user is logged in
        if (loggedInUser == null) {
            log.warn("Unauthorized ticket fetch attempt - no logged in user");
            throw new CustomException("Please log in to book the ticket", ResponseStatus.USER_NOT_FOUND);
        }

        // Call the API and return the response
        return ticketClient.fetchAllTickets(loggedInUser.getTicketsBookedIds());
    }

    /**
     * Cancels a ticket for the logged-in user.
     *
     * @param idOfTicketToCancel The ID of the ticket to cancel.
     * @return ResponseDataDTO containing cancellation result.
     */
    @Override
    public ResponseDataDTO cancelTicket(String idOfTicketToCancel) {
        log.info("Cancelling ticket with ID: {}", idOfTicketToCancel);

        // Ensure the user is logged in
        if (loggedInUser == null) {
            log.warn("Unauthorized ticket cancellation attempt - no logged in user");
            throw new CustomException("Please log in to book the ticket", ResponseStatus.USER_NOT_FOUND);
        }

        // Call the ticket cancellation API using Feign client
        ResponseDataDTO cancelResponse = ticketClient.cancelTicket(idOfTicketToCancel);

        // Remove the ticket from the user's booked list
        loggedInUser.getTicketsBookedIds().removeIf(ticketId -> ticketId.equalsIgnoreCase(idOfTicketToCancel));
        log.info("Updating logged in user ticket list");

        // Save the updated user in the database
        userRepositoryV2.save(loggedInUser);
        log.info("Updating user in the DB");

        return new ResponseDataDTO(true, String.format("Ticket ID: %s has been deleted.", idOfTicketToCancel));
    }

    /**
     * Fetches a ticket by its ID for the logged-in user.
     *
     * @param idOfTicketToFind The ID of the ticket to fetch.
     * @return ResponseDataDTO containing the ticket details.
     */
    @Override
    public ResponseDataDTO fetchTicketById(String idOfTicketToFind) {
        log.info("Fetching ticket by ID: {}", idOfTicketToFind);

        // Ensure the user is logged in
        if (loggedInUser == null) {
            log.warn("Unauthorized ticket fetch attempt - no logged in user");
            throw new CustomException("Please log in to book the ticket", ResponseStatus.USER_NOT_FOUND);
        }

        // Call the API using Feign client and return the response
        return ticketClient.fetchTicketById(idOfTicketToFind);
    }

    /**
     * Reschedules a ticket to a new travel date.
     *
     * @param ticketId          The ID of the ticket to reschedule.
     * @param updatedTravelDate The new travel date.
     * @return ResponseDataDTO containing rescheduling result.
     */
    @Override
    public ResponseDataDTO rescheduleTicket(String ticketId, LocalDate updatedTravelDate) {
        log.info("Rescheduling ticket with ID: {} to new date: {}", ticketId, updatedTravelDate);

        // Ensure the user is logged in
        if (loggedInUser == null) {
            log.warn("Unauthorized ticket rescheduling attempt - no logged in user");
            throw new CustomException("Please log in to book the ticket", ResponseStatus.USER_NOT_FOUND);
        }

        // Ensure the new travel date is not in the past
        if (updatedTravelDate.isBefore(LocalDate.now())) {
            throw new CustomException("Date of travel cannot be in the past", ResponseStatus.INVALID_DATA);
        }

        // Call the ticket rescheduling API using Feign client
        ticketClient.rescheduleTicket(ticketId, updatedTravelDate);

        return new ResponseDataDTO(true, "Travel date updated successfully");
    }
}