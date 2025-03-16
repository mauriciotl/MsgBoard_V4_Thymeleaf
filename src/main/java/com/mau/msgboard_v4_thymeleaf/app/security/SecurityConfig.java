package com.mau.msgboard_v4_thymeleaf.app.security;

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
 */
@Configuration
public class SecurityConfig {

    private static final Logger logger = LogManager.getLogger(SecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Initializing BCryptPasswordEncoder with cost factor 10");
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return username -> {
            logger.debug("Attempting to load user by username: {}", username);
            return userService.findUserByUsername(username)
                    .map(user -> {
                        logger.info("User found - Username: {}", user.getUsername());
                        logger.debug("User found - Username: {}, Password: {}", user.getUsername(), user.getPassword());
                        return (UserDetails) user;
                    })
                    .orElseThrow(() -> {
                        logger.warn("User not found for username: {}", username);
                        return new UsernameNotFoundException("User '" + username + "' not found");
                    });
        };
    }

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
                .logoutUrl("/logout") // Default logout endpoint (POST)
                .logoutSuccessUrl("/") // Redirect after logout
                .addLogoutHandler((request, response, authentication) -> {
                    if (authentication != null) {
                        logger.info("User logged out: {}", authentication.getName());
                    }
                })
                .permitAll() // Allow anyone to access logout
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .build();
    }
}