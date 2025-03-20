package com.yaksh.ticketms.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreeBookedSeatsRequestDTO {
    private String trainPrn;
    private List<List<Integer>> bookedSeatsList;
    private LocalDate travelDate;
}