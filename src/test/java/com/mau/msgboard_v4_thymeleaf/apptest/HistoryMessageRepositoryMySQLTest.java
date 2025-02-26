package com.mau.msgboard_v4_thymeleaf.apptest;

import com.mau.msgboard_v4_thymeleaf.app.dataaccess.HistoryMessageRepositoryMySQL;
import com.mau.msgboard_v4_thymeleaf.app.model.HistoryMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase // Uses H2 by default
@ActiveProfiles("test")
class HistoryMessageRepositoryMySQLTest {

    @Autowired
    private HistoryMessageRepositoryMySQL repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // Clean the table and reset auto-increment
        jdbcTemplate.execute("DROP TABLE IF EXISTS history_message");
        jdbcTemplate.execute("CREATE TABLE history_message (" +
                "history_message_id INT AUTO_INCREMENT PRIMARY KEY," +
                "message_id INT NOT NULL," +
                "content TEXT NOT NULL," +
                "history_creation_date TIMESTAMP NOT NULL," +
                "update_date TIMESTAMP NOT NULL)");
    }

    @Test
    void testSave() {
        // Arrange
        Date now = new Date();
        HistoryMessage message = new HistoryMessage(1, "Test content", now, now);

        // Act
        int id = repository.save(message);

        // Assert
        assertTrue(id > 0);
        HistoryMessage saved = repository.findById(id);
        assertNotNull(saved);
        assertEquals(1, saved.getMessageId());
        assertEquals("Test content", saved.getContent());
    }

    @Test
    void testFindById_NotFound() {
        // Act
        HistoryMessage result = repository.findById(999);

        // Assert
        assertNull(result);
    }

    @Test
    void testFindByMessageId() {
        // Arrange
        Date now = new Date();
        HistoryMessage message1 = new HistoryMessage(1, "Content 1", now, now);
        HistoryMessage message2 = new HistoryMessage(1, "Content 2", now, now);
        repository.save(message1);
        repository.save(message2);

        // Act
        List<HistoryMessage> results = repository.findByMessageId(1);

        // Assert
        assertEquals(2, results.size());
        assertEquals("Content 1", results.get(0).getContent());
        assertEquals("Content 2", results.get(1).getContent());
    }

    @Test
    void testFindByMessageId_NoResults() {
        // Act
        List<HistoryMessage> results = repository.findByMessageId(999);

        // Assert
        assertTrue(results.isEmpty());
    }

    @Test
    void testUpdate() {
        // Arrange
        Date now = new Date();
        HistoryMessage message = new HistoryMessage(1, "Original content", now, now);
        int id = repository.save(message);

        // Act
        message.setHistoryMessageId(id);
        message.setContent("Updated content");
        boolean updated = repository.update(message);

        // Assert
        assertTrue(updated);
        HistoryMessage updatedMessage = repository.findById(id);
        assertEquals("Updated content", updatedMessage.getContent());
    }

    @Test
    void testUpdate_NotFound() {
        // Arrange
        Date now = new Date();
        HistoryMessage message = new HistoryMessage(1, "Content", now, now);
        message.setHistoryMessageId(999);

        // Act
        boolean updated = repository.update(message);

        // Assert
        assertFalse(updated);
    }

    @Test
    void testDeleteById() {
        // Arrange
        Date now = new Date();
        HistoryMessage message = new HistoryMessage(1, "Content", now, now);
        int id = repository.save(message);

        // Act
        boolean deleted = repository.deleteById(id);

        // Assert
        assertTrue(deleted);
        assertNull(repository.findById(id));
    }

    @Test
    void testDeleteById_NotFound() {
        // Act
        boolean deleted = repository.deleteById(999);

        // Assert
        assertFalse(deleted);
    }
}