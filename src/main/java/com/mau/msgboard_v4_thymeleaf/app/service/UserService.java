package com.mau.msgboard_v4_thymeleaf.app.service;

import com.mau.msgboard_v4_thymeleaf.app.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing User entities.
 * Provides basic CRUD operations and additional utility methods for user management.
 */
public interface UserService {

    // Basic CRUD Methods

    /**
     * Creates or updates a user in the system.
     * If the user already exists (e.g., based on userId or username), it updates the existing record.
     *
     * @param user The user to save
     * @return The saved user (may include generated fields like userId if new)
     */
    User saveUser(User user);

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The ID of the user to find
     * @return An Optional containing the user if found, or empty if not
     */
    Optional<User> findUserById(int userId);

    /**
     * Retrieves a user by their username.
     * Useful for authentication and user lookup.
     *
     * @param username The username of the user to find
     * @return An Optional containing the user if found, or empty if not
     */
    Optional<User> findUserByUsername(String username);

    /**
     * Retrieves all users in the system.
     *
     * @return A list of all users
     */
    List<User> findAllUsers();

    /**
     * Deletes a user by their unique identifier.
     *
     * @param userId The ID of the user to delete
     * @return true if the user was deleted, false if not found
     */
    boolean deleteUserById(int userId);

    // Additional Useful Methods

    /**
     * Checks if a user exists by their username.
     * Useful for validation during registration or login.
     *
     * @param username The username to check
     * @return true if the user exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Updates the password for a user identified by their username.
     * Useful for password reset or profile updates.
     *
     * @param username The username of the user
     * @param newPassword The new password (raw, to be encoded by the service)
     * @return true if the password was updated, false if the user wasn't found
     */
    boolean updatePassword(String username, String newPassword);
}