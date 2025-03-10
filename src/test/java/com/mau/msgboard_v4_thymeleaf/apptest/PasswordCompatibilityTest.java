package com.mau.msgboard_v4_thymeleaf.apptest;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordCompatibilityTest {
    public static void main(String[] args) {
        String rawPassword = "pass01";
        String jbcryptHash = BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));
        System.out.println("jbcrypt Hash: " + jbcryptHash);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10); // Match cost factor 10
        System.out.println("Matches jbcrypt hash? " + encoder.matches(rawPassword, jbcryptHash));

        String springHash = encoder.encode(rawPassword);
        System.out.println("Spring Hash: " + springHash);
        System.out.println("Matches Spring hash? " + encoder.matches(rawPassword, springHash));
    }
}