package com.mau.msgboard_v4_thymeleaf.apptest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        // Create an instance of BCryptPasswordEncoder with cost factor 10 (default)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

        // Define the raw password
        String rawPassword = "admin";

        // Generate the hashed password
        String hashedPassword = encoder.encode(rawPassword);

        // Print the result
        System.out.println("Username: admin");
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Hashed Password: " + hashedPassword);
    }
}