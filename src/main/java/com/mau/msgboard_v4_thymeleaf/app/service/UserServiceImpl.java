package com.mau.msgboard_v4_thymeleaf.app.service;

import com.mau.msgboard_v4_thymeleaf.app.dataaccess.UserRepository;
import com.mau.msgboard_v4_thymeleaf.app.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserService for managing User entities.
 * Handles CRUD operations and additional user management tasks, including password encoding.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for dependency injection.
     *
     * @param userRepository The repository for user data access
     * @param passwordEncoder The encoder for securing passwords
     */
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        logger.info("Saving user with username: {}", user.getUsername());
        // Encode the password if it's not already encoded
        if (!isPasswordEncoded(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            logger.debug("Password encoded for user: {}", user.getUsername());
        }
        User savedUser = userRepository.createUser(user);
        logger.info("Successfully saved user with username: {}", savedUser.getUsername());
        return savedUser;
    }

    @Override
    public Optional<User> findUserById(int userId) {
        logger.info("Finding user by ID: {}", userId);
        User user = userRepository.findById(userId);
        if (user != null) {
            logger.info("Found user with ID: {}", userId);
            return Optional.of(user);
        }
        logger.warn("No user found with ID: {}", userId);
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            logger.info("Found user with username: {}", username);
            return Optional.of(user);
        }
        logger.warn("No user found with username: {}", username);
        return Optional.empty();
    }

    @Override
    public List<User> findAllUsers() {
        logger.info("Retrieving all users");
        List<User> users = userRepository.findAll();
        logger.info("Retrieved {} users", users.size());
        return users;
    }

    @Override
    public boolean deleteUserById(int userId) {
        logger.info("Deleting user with ID: {}", userId);
        boolean deleted = userRepository.deleteUser(userId);
        if (deleted) {
            logger.info("Successfully deleted user with ID: {}", userId);
        } else {
            logger.warn("Failed to delete user with ID: {}", userId);
        }
        return deleted;
    }

    @Override
    public boolean existsByUsername(String username) {
        logger.info("Checking if user exists with username: {}", username);
        User user = userRepository.findByUsername(username);
        boolean exists = user != null;
        logger.info("User exists with username: {} - {}", username, exists);
        return exists;
    }

    @Override
    public boolean updatePassword(String username, String newPassword) {
        logger.info("Updating password for user with username: {}", username);
        Optional<User> userOpt = findUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            boolean updated = userRepository.updateUser(user);
            if (updated) {
                logger.info("Successfully updated password for user: {}", username);
            } else {
                logger.warn("Failed to update password for user: {}", username);
            }
            return updated;
        }
        logger.warn("User not found for password update: {}", username);
        return false;
    }

    // Helper method to check if a password is already encoded
    private boolean isPasswordEncoded(String password) {
        // BCrypt hashes start with $2a$ followed by the cost factor (e.g., $2a$10$)
        return password != null && password.startsWith("$2a$");
    }
}