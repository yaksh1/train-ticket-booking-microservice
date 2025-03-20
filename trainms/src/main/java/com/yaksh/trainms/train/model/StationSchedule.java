package com.yaksh.trainms.train.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationSchedule {
    private String name;
    private LocalDateTime arrivalTime;
}
