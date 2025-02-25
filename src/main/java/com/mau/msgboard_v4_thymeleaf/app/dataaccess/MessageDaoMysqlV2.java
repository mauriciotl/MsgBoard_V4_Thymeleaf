package com.mau.msgboard_v4_thymeleaf.app.dataaccess;

import com.mau.msgboard_v4_thymeleaf.app.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository("MessageDaoMysqlV2")
public class MessageDaoMysqlV2 implements MessageDaoV2 {

    // Logger instance for this class
    private static final Logger logger = LogManager.getLogger(MessageDaoMysqlV2.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MessageDaoMysqlV2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.debug("MessageDaoMysqlV2 initialized with JdbcTemplate.");
    }

    @Override
    @Transactional
    public int save(Message message) {
        String sql = "INSERT INTO message (user_id, content, creation_date) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            logger.debug("Attempting to save message: {}", message);
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setLong(1, message.getUserId());
                ps.setString(2, message.getContent());
                ps.setTimestamp(3, new Timestamp(message.getCreationDate().getTime()));
                return ps;
            }, keyHolder);

            int generatedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
            logger.info("Message saved successfully with ID: {}", generatedId);
            return generatedId;

        } catch (DataAccessException e) {
            logger.error("Failed to save message due to a database error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save message due to a database error", e);
        } catch (RuntimeException e) {
            logger.error("Failed to save message due to an unexpected error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save message due to an unexpected error", e);
        }
    }

    @Override
    public Message findById(int messageId) {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        try {
            logger.debug("Attempting to find message by ID: {}", messageId);
            Message message = jdbcTemplate.queryForObject(sql, new Object[]{messageId}, new MessageRowMapper());
            if (message != null) {
                logger.info("Message found with ID: {}", messageId);
            } else {
                logger.warn("No message found with ID: {}", messageId);
            }
            return message;
        } catch (DataAccessException e) {
            logger.error("Database access error while finding message with ID: {}", messageId, e);
            throw new RuntimeException("Database access error", e);
        } catch (RuntimeException e) {
            logger.error("Unexpected error while finding message: {}", e.getMessage(), e);
            throw e; // Propagate the runtime exception
        }
    }




    @Override
    public List<Message> findAll() {
//        String sql = "SELECT * FROM message";
        String sql = "Select * from message order by creation_date desc";
        try {
            logger.debug("Attempting to retrieve all messages.");
            List<Message> messages = jdbcTemplate.query(sql, new MessageRowMapper());
            logger.info("Retrieved {} messages.", messages.size());
            return messages;
        } catch (DataAccessException e) {
            logger.warn("No messages found or error retrieving messages: {}", e.getMessage(), e);
            return null; // Return null if empty message list
        } catch (RuntimeException e) {
            logger.error("Failed to retrieve messages due to an unexpected error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve messages due to an unexpected error", e);
        }
    }

    @Override
    public boolean update(Message message) {
        String sql = "UPDATE message SET user_id = ?, content = ?, creation_date = ? WHERE message_id = ?";
        try {
            logger.debug("Attempting to update message with ID: {}", message.getMessageId());
            int rowsUpdated = jdbcTemplate.update(sql, message.getUserId(), message.getContent(), new Timestamp(message.getCreationDate().getTime()), message.getMessageId());
            boolean updated = rowsUpdated > 0;
            if (updated) {
                logger.info("Message with ID {} updated successfully.", message.getMessageId());
            } else {
                logger.warn("No message found with ID {} to update.", message.getMessageId());
            }
            return updated;
        } catch (DataAccessException e) {
            logger.error("Failed to update message with ID {}: {}", message.getMessageId(), e.getMessage(), e);
            throw new RuntimeException("Failed to update message", e);
        } catch (RuntimeException e) {
            logger.error("Failed to update message due to an unexpected error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update message due to an unexpected error", e);
        }
    }

    @Override
    public boolean delete(int messageId) {
        String sql = "DELETE FROM message WHERE message_id = ?";
        try {
            logger.debug("Attempting to delete message with ID: {}", messageId);
            int rowsDeleted = jdbcTemplate.update(sql, messageId);
            boolean deleted = rowsDeleted > 0;
            if (deleted) {
                logger.info("Message with ID {} deleted successfully.", messageId);
            } else {
                logger.warn("No message found with ID {} to delete.", messageId);
            }
            return deleted;
        } catch (DataAccessException e) {
            logger.error("Failed to delete message with ID {}: {}", messageId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete message", e);
        } catch (RuntimeException e) {
            logger.error("Failed to delete message due to an unexpected error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete message due to an unexpected error", e);
        }
    }

    private static final class MessageRowMapper implements RowMapper<Message> {
        @Override
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("user_id"),
                    rs.getString("content"),
                    rs.getTimestamp("creation_date")
            );
        }
    }
}