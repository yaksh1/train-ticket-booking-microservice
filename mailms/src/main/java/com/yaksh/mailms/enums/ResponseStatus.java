package com.yaksh.mailms.enums;

import org.springframework.http.HttpStatus;

/**
 * Enum representing various response statuses used in the application.
 * Each status is associated with an HTTP status code and a descriptive message.
 */
public enum ResponseStatus {

    // Represents a failure status when an email could not be sent
    MAIL_NOT_SENT(HttpStatus.INTERNAL_SERVER_ERROR, "Error while sending mail");

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