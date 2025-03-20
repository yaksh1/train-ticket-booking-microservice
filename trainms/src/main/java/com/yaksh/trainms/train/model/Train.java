package com.yaksh.trainms.train.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

/**
 * Represents a train entity in the system.
 * This class is mapped to the "trains" collection in MongoDB.
 * It contains information about the train, including its ID, name, seat availability, and schedules.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "trains")
public class Train {

    /** 
     * The unique identifier for the train (Primary Key).
     * This field is annotated with @Id to mark it as the MongoDB document ID.
     */
    @Id
    private String prn;

    /**
     * The name of the train.
     */
    private String trainName;

    /**
     * A map representing the seating arrangement of the train.
     * The key is a string (e.g., coach name), and the value is a list of lists of integers representing seat numbers.
     */
    private Map<String, List<List<Integer>>> seats;

    /**
     * A map representing the train's schedule.
     * The key is a string (e.g., date or route identifier), and the value is a list of StationSchedule objects.
     */
    @Field("schedules")
    private Map<String, List<StationSchedule>> schedules;

    /**
     * Provides a formatted string containing the train's ID.
     * 
     * @return A string in the format "Train ID: {prn}".
     */
    public String getTrainInfo() {
        // Formats and returns the train's ID in a readable string format.
        return String.format("Train ID: %s", prn);
    }
}