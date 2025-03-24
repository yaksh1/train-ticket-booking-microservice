package com.yaksh.userms.user.clients;

import com.yaksh.userms.user.DTO.BookTrainRequestDTO;
import com.yaksh.userms.user.DTO.ResponseDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "TRAINMS")
public interface TrainClient {
    @PostMapping("/v1/seats/book")
    ResponseDataDTO bookSeats(@RequestBody BookTrainRequestDTO requestDTO);
}
