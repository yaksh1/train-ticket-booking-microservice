package com.yaksh.userms.user.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yaksh.userms.user.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for encapsulating response data.
 * This class is used to standardize the structure of API responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties during JSON deserialization.
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON serialization.
public class ResponseDataDTO {
    private boolean status; // Indicates success (true) or failure (false) of the response.
    private ResponseStatus responseStatus; // Enum to represent detailed response status.
    private String message; // A message providing additional information about the response.
    private Object data; // Payload or additional data to be returned in the response.

    /**
     * Constructor to initialize the response with status and message.
     *
     * @param status  Indicates success (true) or failure (false) of the response.
     * @param message A message providing additional information about the response.
     */
    public ResponseDataDTO(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Constructor to initialize the response with status, response status, and message.
     *
     * @param status          Indicates success (true) or failure (false) of the response.
     * @param responseStatus  Enum to represent detailed response status.
     * @param message         A message providing additional information about the response.
     */
    public ResponseDataDTO(boolean status, ResponseStatus responseStatus, String message) {
        this.responseStatus = responseStatus;
        this.status = status;
        this.message = message;
    }

    /**
     * Constructor to initialize the response with status, message, and data.
     *
     * @param status  Indicates success (true) or failure (false) of the response.
     * @param message A message providing additional information about the response.
     * @param data    Payload or additional data to be returned in the response.
     */
    public ResponseDataDTO(boolean status, String message, Object data) {
        this.data = data;
        this.status = status;
        this.message = message;
    }
}