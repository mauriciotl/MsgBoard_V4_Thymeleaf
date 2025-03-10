package com.mau.msgboard_v4_thymeleaf.app.controller;

import com.mau.msgboard_v4_thymeleaf.app.model.User;
import com.mau.msgboard_v4_thymeleaf.app.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling user registration.
 * Provides endpoints for displaying and processing the registration form.
 */
@Controller
public class RegistrationController {

    private static final Logger logger = LogManager.getLogger(RegistrationController.class);

    private final UserService userService;

    /**
     * Constructor for dependency injection.
     *
     * @param userService The service for managing users
     */
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the registration form.
     *
     * @param model The model to pass attributes to the view
     * @return The name of the registration template
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        logger.info("Displaying registration form");
        model.addAttribute("user", new User(0, "", "")); // Empty user for form binding
        return "registration"; // Maps to registration.html
    }

    /**
     * Processes the registration form submission.
     * Validates input and creates a new user if the username is available.
     *
     * @param username The username from the form
     * @param password The password from the form
     * @param model The model to pass attributes to the view
     * @return Redirect to login on success, or back to registration with error
     */
    @PostMapping("/register")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model) {
        logger.info("Processing registration for username: {}", username);

        // Basic validation
        if (username == null || username.trim().isEmpty()) {
            logger.warn("Registration failed: Username is empty");
            model.addAttribute("error", "Username cannot be empty");
            return "registration";
        }
        if (password == null || password.trim().isEmpty()) {
            logger.warn("Registration failed: Password is empty");
            model.addAttribute("error", "Password cannot be empty");
            return "registration";
        }

        // Check if username is taken
        if (userService.existsByUsername(username)) {
            logger.warn("Registration failed: Username {} already exists", username);
            model.addAttribute("error", "Username is already taken");
            return "registration";
        }

        // Create and save the new user
        User newUser = new User(0, username, password); // userId will be set by DB if auto-increment
        try {
            userService.saveUser(newUser);
            logger.info("Successfully registered user: {}", username);
            return "redirect:/login?registered"; // Redirect to login with success message
        } catch (Exception e) {
            logger.error("Error registering user: {}", username, e);
            model.addAttribute("error", "An error occurred during registration");
            return "registration";
        }
    }
}