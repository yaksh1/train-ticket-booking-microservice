package com.yaksh.userms.user.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * Service utility class for user-related operations such as password hashing and validation.
 */
@Service
public class UserServiceUtilImpl implements UserServiceUtil {

    /**
     * Hashes the given plain text password using the BCrypt hashing algorithm.
     *
     * @param password the plain text password to be hashed
     * @return the hashed password
     */
    @Override
    public String hashPassword(String password) {
        // Generate a salted hash of the password using BCrypt and return it
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Validates a plain text password against a previously hashed password.
     *
     * @param password       the plain text password to validate
     * @param hashedPassword the hashed password to compare against
     * @return true if the password matches the hashed password, false otherwise
     */
    @Override
    public boolean checkPassword(String password, String hashedPassword) {
        // Use BCrypt to check if the plain text password matches the hashed password
        return BCrypt.checkpw(password, hashedPassword);
    }
}