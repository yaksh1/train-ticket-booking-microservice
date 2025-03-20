package com.yaksh.trainms.train.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents the schedule of a station in a train's journey.
 * Contains the station name and the arrival time at that station.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationSchedule {
    
    /** 
     * The name of the station. 
     */
    private String name;

    /**
     * The time at which the train is scheduled to arrive at the station.
     */
    private LocalDateTime arrivalTime;
}