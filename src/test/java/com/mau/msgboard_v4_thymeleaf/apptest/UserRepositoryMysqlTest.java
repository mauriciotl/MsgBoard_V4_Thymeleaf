package com.mau.msgboard_v4_thymeleaf.apptest;

import com.mau.msgboard_v4_thymeleaf.app.dataaccess.UserRepositoryMysql;
import com.mau.msgboard_v4_thymeleaf.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryMysqlTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserRepositoryMysql userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1, "testUser", "password123");
    }

    // Unit Tests with Mockito
    @Test
    void createUser_Successful() {
        when(jdbcTemplate.update(anyString(), anyInt(), anyString(), anyString()))
                .thenReturn(1);

        User result = userRepository.createUser(testUser);

        assertEquals(testUser, result);
        verify(jdbcTemplate).update(
                eq("INSERT INTO user (user_id, name, password) VALUES (?, ?, ?)"),
                eq(1),
                eq("testUser"),
                eq("password123")
        );
    }

    @Test
    void createUser_ThrowsException() {
        when(jdbcTemplate.update(anyString(), anyInt(), anyString(), anyString()))
                .thenThrow(new RuntimeException("DB Error"));

        assertThrows(RuntimeException.class, () -> userRepository.createUser(testUser));
    }

    @Test
    void findByUsername_Successful() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyString()))
                .thenReturn(testUser);

        User result = userRepository.findByUsername("testUser");

        assertNotNull(result);
        assertEquals(testUser, result);
        verify(jdbcTemplate).queryForObject(
                eq("SELECT * FROM user WHERE name = ?"),
                any(RowMapper.class),
                eq("testUser")
        );
    }

    @Test
    void findByUsername_NotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyString()))
                .thenThrow(new RuntimeException("Not found"));

        User result = userRepository.findByUsername("nonexistent");

        assertNull(result);
    }

    @Test
    void findById_Successful() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(testUser);

        User result = userRepository.findById(1);

        assertNotNull(result);
        assertEquals(testUser, result);
    }

    @Test
    void findAll_Successful() {
        List<User> users = Collections.singletonList(testUser);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(users);

        List<User> result = userRepository.findAll();

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
    }

    @Test
    void updateUser_Successful() {
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(1);

        boolean result = userRepository.updateUser(testUser);

        assertTrue(result);
        verify(jdbcTemplate).update(
                eq("UPDATE user SET name = ?, password = ? WHERE user_id = ?"),
                eq("testUser"),
                eq("password123"),
                eq(1)
        );
    }

    @Test
    void deleteUserById_Successful() {
        when(jdbcTemplate.update(anyString(), anyInt()))
                .thenReturn(1);

        boolean result = userRepository.deleteUser(1);

        assertTrue(result);
        verify(jdbcTemplate).update(
                eq("DELETE FROM user WHERE user_id = ?"),
                eq(1)
        );
    }

    @Test
    void deleteUserByObject_Successful() {
        when(jdbcTemplate.update(anyString(), anyInt()))
                .thenReturn(1);

        boolean result = userRepository.deleteUser(testUser);

        assertTrue(result);
        verify(jdbcTemplate).update(
                eq("DELETE FROM user WHERE user_id = ?"),
                eq(1)
        );
    }
}