package com.mau.msgboard_v4_thymeleaf.app.security;

import com.mau.msgboard_v4_thymeleaf.app.dataaccess.UserRepository;
import com.mau.msgboard_v4_thymeleaf.app.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

    // Logger instance for Log4j2 to log security-related events
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
     * Configures the UserDetailsService bean to load users from the UserRepository.
     * This is used by Spring Security to authenticate users during login.
     *
     * @param userRepo The repository for accessing user data
     * @return A UserDetailsService implementation
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            logger.debug("Attempting to load user by username: {}", username);
            User user = userRepo.findByUsername(username);
            if (user != null) {
                logger.info("User found - Username: {}, Password: {}", user.getUsername(), user.getPassword());
                return user;
            }
            logger.warn("User not found for username: {}", username);
            throw new UsernameNotFoundException("User '" + username + "' not found");
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
                // Authorization rules for HTTP requests
                .authorizeRequests()
                .mvcMatchers("/messages", "/history").hasRole("USER") // Restrict to USER role
                .anyRequest().permitAll() // Allow all other requests without authentication
                .and()
                // Form-based login configuration
                .formLogin()
                .loginPage("/login") // Custom login page
                // Custom failure handler to log failed login attempts
                .failureHandler((request, response, exception) -> {
                    String username = request.getParameter("username");
                    logger.error("Login attempt failed - Username: {}, Reason: {}", username, exception.getMessage());
                    response.sendRedirect(request.getContextPath() + "/login?error");
                })
                // Custom success handler to log successful logins and redirect
                .successHandler((request, response, authentication) -> {
                    logger.info("Login successful for: {}", authentication.getName());
                    response.sendRedirect(request.getContextPath() + "/messages");
                })
                .and()
                // Logout configuration
                .logout()
                .logoutSuccessUrl("/") // Redirect to root after logout
                .and()
                // HTTP headers configuration
                .headers()
                .frameOptions()
                .sameOrigin() // Allow same-origin framing (e.g., for H2 console if used)
                .and()
                // Build the filter chain
                .build();
    }
}