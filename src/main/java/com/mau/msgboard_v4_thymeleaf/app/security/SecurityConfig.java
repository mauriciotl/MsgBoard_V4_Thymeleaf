package com.mau.msgboard_v4_thymeleaf.app.security;

import com.mau.msgboard_v4_thymeleaf.app.model.User;
import com.mau.msgboard_v4_thymeleaf.app.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security settings.
 * Defines authentication and authorization rules, password encoding, and custom login/logout behavior.
 */
@Configuration
public class SecurityConfig {

    private static final Logger logger = LogManager.getLogger(SecurityConfig.class);

    /**
     * Defines the password encoder bean using BCrypt with a cost factor of 10.
     * This ensures passwords are hashed securely and matches the cost factor used in jbcrypt hashes.
     *
     * @return A BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Initializing BCryptPasswordEncoder with cost factor 10");
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Configures the UserDetailsService bean to load users using UserService.
     * This is used by Spring Security to authenticate users during login.
     *
     * @param userService The service for managing user data
     * @return A UserDetailsService implementation
     */
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return username -> {
            logger.debug("Attempting to load user by username: {}", username);
            return userService.findUserByUsername(username)
                    .map(user -> {
                        logger.info("User found - Username: {}, Password: {}", user.getUsername(), user.getPassword());
                        return (UserDetails) user; // Cast to UserDetails since User implements it
                    })
                    .orElseThrow(() -> {
                        logger.warn("User not found for username: {}", username);
                        return new UsernameNotFoundException("User '" + username + "' not found");
                    });
        };
    }

    /**
     * Configures the security filter chain for HTTP requests.
     * Defines which endpoints require authentication, custom login/logout behavior, and headers.
     *
     * @param http The HttpSecurity object to configure
     * @return A SecurityFilterChain instance
     * @throws Exception If configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring SecurityFilterChain");
        return http
                .authorizeRequests()
                .mvcMatchers("/messages", "/history").hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureHandler((request, response, exception) -> {
                    String username = request.getParameter("username");
                    logger.error("Login attempt failed - Username: {}, Reason: {}", username, exception.getMessage());
                    response.sendRedirect(request.getContextPath() + "/login?error");
                })
                .successHandler((request, response, authentication) -> {
                    logger.info("Login successful for: {}", authentication.getName());
                    response.sendRedirect(request.getContextPath() + "/messages");
                })
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .build();
    }
}