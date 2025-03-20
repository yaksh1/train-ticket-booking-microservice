package com.yaksh.ticketms.ticket.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yaksh.ticketms.ticket.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for encapsulating response data.
 * Used to standardize the structure of responses sent from the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown JSON properties during deserialization.
@JsonInclude(JsonInclude.Include.NON_NULL) // Include only non-null properties in the JSON output.
public class ResponseDataDTO {

    /**
     * Indicates whether the operation was successful or not.
     */
    private boolean status;

    /**
     * Enum representing the status of the response (e.g., SUCCESS, FAILURE).
     */
    private ResponseStatus responseStatus;

    /**
     * Message providing additional information about the response.
     */
    private String message;

    /**
     * Any additional data associated with the response.
     */
    private Object data;

    /**
     * Constructor to initialize the response with status and message.
     *
     * @param status  Indicates success or failure of the operation.
     * @param message Additional information about the response.
     */
    public ResponseDataDTO(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Constructor to initialize the response with status, response status, and message.
     *
     * @param status          Indicates success or failure of the operation.
     * @param responseStatus  Enum representing the status of the response.
     * @param message         Additional information about the response.
     */
    public ResponseDataDTO(boolean status, ResponseStatus responseStatus, String message) {
        this.responseStatus = responseStatus; // Set the response status enum value.
        this.status = status;
        this.message = message;
    }

    /**
     * Constructor to initialize the response with status, message, and additional data.
     *
     * @param status  Indicates success or failure of the operation.
     * @param message Additional information about the response.
     * @param data    Additional data associated with the response.
     */
    public ResponseDataDTO(boolean status, String message, Object data) {
        this.data = data; // Set the additional data for the response.
        this.status = status;
        this.message = message;
    }
}