package com.mau.msgboard_v4_thymeleaf.app.dataaccess;

import com.mau.msgboard_v4_thymeleaf.app.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class UserRepositoryMysql implements UserRepository {

    private static final Logger logger = LogManager.getLogger(UserRepositoryMysql.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // SQL queries
    private static final String INSERT_USER = "INSERT INTO user (user_id, username, password) VALUES (?, ?, ?)";
    private static final String SELECT_BY_USERNAME = "SELECT * FROM user WHERE username = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM user WHERE user_id = ?";
    private static final String SELECT_ALL = "SELECT * FROM user";
    private static final String UPDATE_USER = "UPDATE user SET username = ?, password = ? WHERE user_id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM user WHERE user_id = ?";

    // RowMapper for mapping ResultSet to User object
    private static final RowMapper<User> USER_ROW_MAPPER = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password")
            );
        }
    };

    @Override
    public User createUser(User user) {
        logger.info("Attempting to create user with username: {}", user.getUsername());
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        INSERT_USER, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, user.getUserId()); // Will be ignored if auto-increment
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getPassword());
                return ps;
            }, keyHolder);
            int generatedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
            logger.info("Successfully created user with username: {} and ID: {}", user.getUsername(), generatedId);
            return new User(generatedId, user.getUsername(), user.getPassword());
        } catch (Exception e) {
            logger.error("Error creating user with username: {}. Error: {}", user.getUsername(), e.getMessage());
            throw new RuntimeException("Failed to create user", e);
        }
    }

    @Override
    public User findByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        try {
            User user = jdbcTemplate.queryForObject(SELECT_BY_USERNAME,
                    USER_ROW_MAPPER, username);
            logger.info("Successfully found user with username: {}", username);
            return user;
        } catch (Exception e) {
            logger.warn("User not found or error occurred for username: {}. Error: {}",
                    username, e.getMessage());
            return null; // or throw exception based on your requirements
        }
    }

    @Override
    public User findById(int userId) {
        logger.info("Finding user by ID: {}", userId);
        try {
            User user = jdbcTemplate.queryForObject(SELECT_BY_ID,
                    USER_ROW_MAPPER, userId);
            logger.info("Successfully found user with ID: {}", userId);
            return user;
        } catch (Exception e) {
            logger.warn("User not found or error occurred for ID: {}. Error: {}",
                    userId, e.getMessage());
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        logger.info("Retrieving all users");
        try {
            List<User> users = jdbcTemplate.query(SELECT_ALL, USER_ROW_MAPPER);
            logger.info("Successfully retrieved {} users", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Error retrieving all users: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    @Override
    public boolean updateUser(User user) {
        logger.info("Attempting to update user with ID: {}", user.getUserId());
        try {
            int rowsAffected = jdbcTemplate.update(UPDATE_USER,
                    user.getUsername(),
                    user.getPassword(),
                    user.getUserId()
            );
            boolean success = rowsAffected > 0;
            if (success) {
                logger.info("Successfully updated user with ID: {}", user.getUserId());
            } else {
                logger.warn("No user updated with ID: {}", user.getUserId());
            }
            return success;
        } catch (Exception e) {
            logger.error("Error updating user with ID: {}. Error: {}",
                    user.getUserId(), e.getMessage());
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        logger.info("Attempting to delete user with ID: {}", userId);
        try {
            int rowsAffected = jdbcTemplate.update(DELETE_BY_ID, userId);
            boolean success = rowsAffected > 0;
            if (success) {
                logger.info("Successfully deleted user with ID: {}", userId);
            } else {
                logger.warn("No user deleted with ID: {}", userId);
            }
            return success;
        } catch (Exception e) {
            logger.error("Error deleting user with ID: {}. Error: {}",
                    userId, e.getMessage());
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    @Override
    public boolean deleteUser(User user) {
        logger.info("Attempting to delete user with username: {}", user.getUsername());
        return deleteUser(user.getUserId());
    }
}