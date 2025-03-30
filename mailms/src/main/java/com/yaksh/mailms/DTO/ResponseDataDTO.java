package com.yaksh.mailms.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yaksh.mailms.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for encapsulating response data.
 * This class is used to standardize the response structure sent back to the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown JSON properties during deserialization.
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from the JSON response.
public class ResponseDataDTO {

    // Indicates whether the response is successful or not.
    private boolean status;

    // Enum representing the status of the response.
    private ResponseStatus responseStatus;

    // A message providing additional information about the response.
    private String message;

    // Generic object to hold any data associated with the response.
    private Object data;

    /**
     * Constructor to create a ResponseDataDTO with status and message.
     *
     * @param status  Indicates whether the response is successful or not.
     * @param message A message providing additional information about the response.
     */
    public ResponseDataDTO(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Constructor to create a ResponseDataDTO with status, responseStatus, and message.
     *
     * @param status          Indicates whether the response is successful or not.
     * @param responseStatus  Enum representing the status of the response.
     * @param message         A message providing additional information about the response.
     */
    public ResponseDataDTO(boolean status, ResponseStatus responseStatus, String message) {
        this.responseStatus = responseStatus;
        this.status = status;
        this.message = message;
    }

    /**
     * Constructor to create a ResponseDataDTO with status, message, and data.
     *
     * @param status  Indicates whether the response is successful or not.
     * @param message A message providing additional information about the response.
     * @param data    Generic object to hold any data associated with the response.
     */
    public ResponseDataDTO(boolean status, String message, Object data) {
        this.data = data;
        this.status = status;
        this.message = message;
    }
}