package com.yaksh.userms.user.util;

/**
 * Utility interface for user service operations.
 * Provides methods for password hashing and validation.
 */
public interface UserServiceUtil {

    /**
     * Hashes a plain text password.
     * 
     * @param password The plain text password to be hashed.
     * @return A hashed representation of the password.
     */
    String hashPassword(String password);

    /**
     * Validates a plain text password against a hashed password.
     * 
     * @param password The plain text password to validate.
     * @param hashedPassword The hashed password to compare against.
     * @return true if the password matches the hashed password, false otherwise.
     */
    boolean checkPassword(String password, String hashedPassword);

}