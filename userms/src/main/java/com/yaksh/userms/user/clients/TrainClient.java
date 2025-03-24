package com.yaksh.userms.user.clients;

import com.yaksh.userms.user.DTO.BookTrainRequestDTO;
import com.yaksh.userms.user.DTO.ResponseDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client interface for interacting with the Train Microservice (TRAINMS).
 * This interface is used to communicate with the TRAINMS for booking train seats.
 * It abstracts the HTTP communication details and provides a declarative way to call the Train Microservice.
 */
@FeignClient(name = "TRAINMS")
public interface TrainClient {

    /**
     * Sends a POST request to the TRAINMS to book train seats.
     *
     * @param requestDTO The request payload containing the details required to book seats.
     *                   This includes information such as train ID, user details, and seat preferences.
     * @return ResponseDataDTO containing the response data from the TRAINMS.
     *         The response typically indicates whether the booking was successful or not.
     */
    @PostMapping("/v1/seats/book")
    ResponseDataDTO bookSeats(@RequestBody BookTrainRequestDTO requestDTO);
}