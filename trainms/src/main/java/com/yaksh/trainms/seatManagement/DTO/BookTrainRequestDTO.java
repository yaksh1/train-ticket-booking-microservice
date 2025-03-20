package com.yaksh.trainms.seatManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookTrainRequestDTO {
    private String userId;
    private String trainPrn;
    private String source;
    private String destination;
    private LocalDate travelDate;
    private int numberOfSeatsToBeBooked;
}

