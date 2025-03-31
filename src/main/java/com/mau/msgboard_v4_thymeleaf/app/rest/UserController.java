package com.mau.msgboard_v4_thymeleaf.app.rest;

import com.mau.msgboard_v4_thymeleaf.app.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@RestController
@RequestMapping("/rest/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> getUsername(@PathVariable int userId) {
        logger.info("Received request to get username for userId: {}", userId);
        return userService.findUserById(userId)
                .map(user -> {
                    logger.info("User found: {}", user.getUsername());
                    return ResponseEntity.ok(user.getUsername());
                })
                .orElseGet(() -> {
                    logger.warn("User not found for userId: {}", userId);
                    return ResponseEntity.notFound().build();
                });
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (Objects.equals(ex.getParameter().getParameterName(), "userId")) {
            String errorMessage = "Invalid userId: '" + ex.getValue() + "'. It must be a valid integer.";
            logger.error("Type mismatch error: {}", errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
        logger.error("Unexpected type mismatch error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Invalid request parameter.");
    }
}
