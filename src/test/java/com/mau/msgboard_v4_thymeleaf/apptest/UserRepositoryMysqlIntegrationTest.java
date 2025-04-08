package com.mau.msgboard_v4_thymeleaf.apptest;

import com.mau.msgboard_v4_thymeleaf.app.dataaccess.UserRepositoryMysql;
import com.mau.msgboard_v4_thymeleaf.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Disabled("Skipping all MessageController tests until controller implementation is complete")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UserRepositoryMysqlIntegrationTest {

    @Autowired
    private UserRepositoryMysql userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // Clean up and set up test data
        jdbcTemplate.execute("DROP TABLE IF EXISTS user");
        jdbcTemplate.execute(
                "CREATE TABLE user (user_id INT PRIMARY KEY, name VARCHAR(255), password VARCHAR(255))"
        );
    }

    @Test
    void createAndFindUser_Integration() {
        User user = new User(1, "integrationUser", "pass123");

        userRepository.createUser(user);
        User foundUser = userRepository.findByUsername("integrationUser");

        assertNotNull(foundUser);
        assertEquals("integrationUser", foundUser.getUsername());
        assertEquals("pass123", foundUser.getPassword());
    }
}