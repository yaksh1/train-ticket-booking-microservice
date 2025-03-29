package com.yaksh.trainms.train.enums;

import org.springframework.http.HttpStatus;

/**
 * Enum representing various response statuses used in the application.
 * Each status is associated with an HTTP status code and a descriptive message.
 */
public enum ResponseStatus {

    // Trains
    NOT_ENOUGH_SEATS(HttpStatus.INTERNAL_SERVER_ERROR, "Not enough seats available"), // Indicates insufficient seats for a train
    TRAIN_NOT_FOUND(HttpStatus.NOT_FOUND, "Train not found"), // Indicates that the train was not found in the system
    TRAIN_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Train already exists"), // Indicates that the train already exists in the system
    TRAIN_NOT_SAVED_IN_COLLECTION(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save train in collection"), // Indicates a failure in saving train data
    TRAIN_UPDATING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Train update failed"), // Indicates a failure in updating train data

    // General
    INVALID_DATA(HttpStatus.BAD_REQUEST, "Invalid input data"), // Indicates that the input data provided is invalid
    TICKET_NOT_CREATED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create ticket"), // Indicates a failure in creating a ticket
    FREE_THE_SEAT_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"Freeing the seats failed" ); // Indicates a failure in freeing the seats

    private final HttpStatus httpStatus; // HTTP status code associated with the response
    private final String message; // Descriptive message for the response status

    /**
     * Constructor for the ResponseStatus enum.
     *
     * @param httpStatus The HTTP status code associated with the response.
     * @param message    The descriptive message for the response status.
     */
    ResponseStatus(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    /**
     * Gets the HTTP status code associated with the response status.
     *
     * @return The HTTP status code.
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * Gets the descriptive message for the response status.
     *
     * @return The descriptive message.
     */
    public String getMessage() {
        return message;
    }
}