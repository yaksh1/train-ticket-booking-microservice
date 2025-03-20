package com.yaksh.trainms.train.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the structure for error details in the application.
 * This class is used to encapsulate error information such as status, message, code, and timestamp.
 * It is annotated with Lombok annotations to reduce boilerplate code.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
  
  /** 
   * The status of the error (e.g., "FAILURE" or "ERROR").
   * Indicates the overall outcome of the operation that caused the error.
   */
  private String status;
  
  /**
   * A descriptive error message providing details about the error.
   * Helps in understanding the nature of the issue.
   */
  private String errorMessage;
  
  /**
   * The HTTP status code associated with the error (e.g., 404, 500).
   * This is useful for categorizing errors based on standard HTTP response codes.
   */
  private int statusCode;
  
  /**
   * A specific error code that uniquely identifies the error type.
   * Can be used for programmatic handling of specific error scenarios.
   */
  private String errorCode;
  
  /**
   * The timestamp indicating when the error occurred.
   * Useful for logging and debugging purposes to track the exact time of the error.
   */
  private LocalDateTime timeStamp;
}