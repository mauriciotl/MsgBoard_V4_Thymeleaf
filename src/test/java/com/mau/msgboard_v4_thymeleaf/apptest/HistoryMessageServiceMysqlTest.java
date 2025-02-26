package com.mau.msgboard_v4_thymeleaf.apptest;

import com.mau.msgboard_v4_thymeleaf.app.dataaccess.HistoryMessageRepository;
import com.mau.msgboard_v4_thymeleaf.app.model.HistoryMessage;
import com.mau.msgboard_v4_thymeleaf.app.service.HistoryMessageServiceMysql;
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
class HistoryMessageServiceMysqlTest {

    @Autowired
    private HistoryMessageServiceMysql historyMessageService;

    @Autowired
    private HistoryMessageRepository historyMessageRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // Clean and recreate the table before each test
        jdbcTemplate.execute("DROP TABLE IF EXISTS history_message");
        jdbcTemplate.execute("CREATE TABLE history_message (" +
                "history_message_id INT AUTO_INCREMENT PRIMARY KEY," +
                "message_id INT NOT NULL," +
                "content TEXT NOT NULL," +
                "history_creation_date TIMESTAMP NOT NULL," +
                "update_date TIMESTAMP NOT NULL)");
    }

    @Test
    void testSaveHistoryMessage() {
        // Arrange
        Date now = new Date();
        HistoryMessage message = new HistoryMessage(1, "Test content", now, now);

        // Act
        int id = historyMessageService.saveHistoryMessage(message);

        // Assert
        assertTrue(id > 0);
        HistoryMessage saved = historyMessageService.getHistoryMessageById(id);
        assertNotNull(saved);
        assertEquals(1, saved.getMessageId());
        assertEquals("Test content", saved.getContent());
    }

//    @Test
//    void testSaveHistoryMessage_NullInput() {
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> historyMessageService.saveHistoryMessage(null));
//    }

    @Test
    void testSaveHistoryMessage_NullInput() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> historyMessageService.saveHistoryMessage(null));
    }


    @Test
    void testGetHistoryMessageById() {
        // Arrange
        Date now = new Date();
        HistoryMessage message = new HistoryMessage(1, "Test content", now, now);
        int id = historyMessageService.saveHistoryMessage(message);

        // Act
        HistoryMessage retrieved = historyMessageService.getHistoryMessageById(id);

        // Assert
        assertNotNull(retrieved);
        assertEquals(id, retrieved.getHistoryMessageId());
        assertEquals("Test content", retrieved.getContent());
    }

    @Test
    void testGetHistoryMessageById_InvalidId() {
        // Act
        HistoryMessage result = historyMessageService.getHistoryMessageById(-1);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetHistoryMessagesByMessageId() {
        // Arrange
        Date now = new Date();
        HistoryMessage message1 = new HistoryMessage(1, "Content 1", now, now);
        HistoryMessage message2 = new HistoryMessage(1, "Content 2", now, now);
        historyMessageService.saveHistoryMessage(message1);
        historyMessageService.saveHistoryMessage(message2);

        // Act
        List<HistoryMessage> results = historyMessageService.getHistoryMessagesByMessageId(1);

        // Assert
        assertEquals(2, results.size());
        assertEquals("Content 1", results.get(0).getContent());
        assertEquals("Content 2", results.get(1).getContent());
    }

    @Test
    void testGetHistoryMessagesByMessageId_InvalidId() {
        // Act
        List<HistoryMessage> results = historyMessageService.getHistoryMessagesByMessageId(-1);

        // Assert
        assertTrue(results.isEmpty());
    }

    @Test
    void testUpdateHistoryMessage() {
        // Arrange
        Date now = new Date();
        HistoryMessage message = new HistoryMessage(1, "Original", now, now);
        int id = historyMessageService.saveHistoryMessage(message);
        message.setHistoryMessageId(id);
        message.setContent("Updated");

        // Act
        boolean updated = historyMessageService.updateHistoryMessage(message);

        // Assert
        assertTrue(updated);
        HistoryMessage retrieved = historyMessageService.getHistoryMessageById(id);
        assertEquals("Updated", retrieved.getContent());
    }

    @Test
    void testUpdateHistoryMessage_InvalidId() {
        // Arrange
        Date now = new Date();
        HistoryMessage message = new HistoryMessage(1, "Content", now, now);
        message.setHistoryMessageId(-1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> historyMessageService.updateHistoryMessage(message));
    }

    @Test
    void testDeleteHistoryMessage() {
        // Arrange
        Date now = new Date();
        HistoryMessage message = new HistoryMessage(1, "Content", now, now);
        int id = historyMessageService.saveHistoryMessage(message);

        // Act
        boolean deleted = historyMessageService.deleteHistoryMessage(id);

        // Assert
        assertTrue(deleted);
        assertNull(historyMessageService.getHistoryMessageById(id));
    }

    @Test
    void testDeleteHistoryMessage_InvalidId() {
        // Act
        boolean deleted = historyMessageService.deleteHistoryMessage(-1);

        // Assert
        assertFalse(deleted);
    }
}