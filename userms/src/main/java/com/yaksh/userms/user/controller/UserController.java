package com.yaksh.userms.user.controller;

import com.yaksh.userms.user.DTO.ResponseDataDTO;
import com.yaksh.userms.user.model.User;
import com.yaksh.userms.user.service.UserBookingService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller class for handling user-related operations such as login, signup, ticket booking, and ticket management.
 */
@RestController
@RequestMapping("/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserBookingService service;

    /**
     * Logs in the user by verifying the provided username and password.
     * If login is successful, sets the logged-in user in the service layer.
     *
     * @param userEmail The username of the user trying to log in.
     * @param password The password of the user trying to log in.
     * @return ResponseEntity containing the login status and user information.
     */
    @PostMapping("/loginUser")
    public ResponseEntity<ResponseDataDTO> loginUser(@RequestParam String userEmail, @RequestParam String password) {
        // Call the service layer to handle user login and return the response.
        ResponseDataDTO responseDataDTO = service.loginUser(userEmail, password);
        return ResponseEntity.ok(responseDataDTO);
    }

    /**
     * Signs up a new user with the provided username and password.
     *
     * @param userEmail The username of the user to be registered.
     * @param password The password for the new user.
     * @return ResponseEntity containing the signup status.
     */
    @PostMapping("/signupUser")
    public ResponseEntity<ResponseDataDTO> signupUser(@RequestParam String userEmail, @RequestParam String password) {
        // Call the service layer to handle user signup and return the response.
        return ResponseEntity.ok(service.signupUser(userEmail, password));
    }

    /**
     * Books a ticket for the user with the provided travel details.
     *
     * @param trainPrn               The train PRN (Passenger Reservation Number) to book the ticket for.
     * @param source                 The source station of the journey.
     * @param destination            The destination station of the journey.
     * @param dateOfTravel           The date of travel for the ticket.
     * @param numberOfSeatsToBeBooked The number of seats to be booked.
     * @return ResponseEntity containing the booking status and ticket details.
     */
    @PostMapping("/bookTicket")
    public ResponseEntity<ResponseDataDTO> bookTicket(
            @RequestParam String trainPrn,
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfTravel,
            @RequestParam int numberOfSeatsToBeBooked) {
        // Call the service layer to handle ticket booking and return the response.
        return ResponseEntity.ok(
                service.bookTicket(trainPrn, source, destination, dateOfTravel, numberOfSeatsToBeBooked));
    }

    /**
     * Fetches all the tickets booked by the logged-in user.
     *
     * @return ResponseEntity containing the list of all tickets booked by the user.
     */
    @GetMapping("/fetchTickets")
    public ResponseEntity<ResponseDataDTO> fetchAllTickets() {
        // Call the service layer to fetch all tickets for the logged-in user and return the response.
        return ResponseEntity.ok(service.fetchAllTickets());
    }

    /**
     * Cancels a ticket with the provided ticket ID.
     *
     * @param ticketId The ID of the ticket to be canceled.
     * @return ResponseEntity containing the cancellation status.
     */
    @PostMapping("/cancelTicket")
    public ResponseEntity<ResponseDataDTO> cancelTicket(@RequestParam String ticketId) {
        // Call the service layer to handle ticket cancellation and return the response.
        return ResponseEntity.ok(service.cancelTicket(ticketId));
    }

    /**
     * Fetches the details of a ticket by its ID.
     *
     * @param ticketId The ID of the ticket to fetch details for.
     * @return ResponseEntity containing the ticket details.
     */
    @GetMapping("/fetchTicketById")
    public ResponseEntity<ResponseDataDTO> fetchTicketById(@RequestParam String ticketId) {
        // Call the service layer to fetch ticket details by ID and return the response.
        return ResponseEntity.ok(service.fetchTicketById(ticketId));
    }

    /**
     * Reschedules a ticket to a new date of travel.
     *
     * @param ticketId             The ID of the ticket to be rescheduled.
     * @param updatedDateOfTravel  The new date of travel for the ticket.
     * @return ResponseEntity containing the rescheduling status.
     */
    @PostMapping("/rescheduleTicket")
    public ResponseEntity<ResponseDataDTO> rescheduleTicket(
            @RequestParam String ticketId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedDateOfTravel) {
        // Call the service layer to handle ticket rescheduling and return the response.
        return ResponseEntity.ok(service.rescheduleTicket(ticketId, updatedDateOfTravel));
    }
}