package com.mau.msgboard_v4_thymeleaf.app.dataaccess;

import com.mau.msgboard_v4_thymeleaf.app.model.HistoryMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class HistoryMessageRepositoryMySQL implements HistoryMessageRepository {

    private static final Logger logger = LogManager.getLogger(HistoryMessageRepositoryMySQL.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HistoryMessageRepositoryMySQL(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper to convert ResultSet to HistoryMessage object
    private static final RowMapper<HistoryMessage> HISTORY_MESSAGE_MAPPER = (rs, rowNum) -> {
        HistoryMessage message = new HistoryMessage();
        message.setHistoryMessageId(rs.getInt("history_message_id"));
        message.setMessageId(rs.getInt("message_id"));
        message.setContent(rs.getString("content"));
        message.setHistoryCreationDate(rs.getTimestamp("history_creation_date"));
        message.setUpdateDate(rs.getTimestamp("update_date"));
        return message;
    };

    @Override
    public int save(HistoryMessage historyMessage) {
        String sql = "INSERT INTO history_message (message_id, content, history_creation_date, update_date) " +
                "VALUES (?, ?, ?, ?)";

        logger.debug("Attempting to save HistoryMessage for messageId: {}", historyMessage.getMessageId());

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"history_message_id"});
                ps.setInt(1, historyMessage.getMessageId());
                ps.setString(2, historyMessage.getContent());
                ps.setTimestamp(3, new Timestamp(historyMessage.getHistoryCreationDate().getTime()));
                ps.setTimestamp(4, new Timestamp(historyMessage.getUpdateDate().getTime()));
                return ps;
            }, keyHolder);

            int generatedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
            logger.info("Successfully saved HistoryMessage with ID: {}", generatedId);
            return generatedId;

        } catch (DataAccessException e) {
            logger.error("Failed to save HistoryMessage for messageId: {}. Error: {}",
                    historyMessage.getMessageId(), e.getMessage());
            throw new RuntimeException("Failed to save history message", e);
        }
    }

    @Override
    public HistoryMessage findById(int historyMessageId) {
        String sql = "SELECT * FROM history_message WHERE history_message_id = ?";

        logger.debug("Finding HistoryMessage by ID: {}", historyMessageId);

        try {
            HistoryMessage result = jdbcTemplate.queryForObject(sql, HISTORY_MESSAGE_MAPPER, historyMessageId);
            logger.info("Successfully retrieved HistoryMessage with ID: {}", historyMessageId);
            return result;
        } catch (DataAccessException e) {
            logger.warn("No HistoryMessage found for ID: {}. Error: {}", historyMessageId, e.getMessage());
            return null;
        }
    }

    @Override
    public List<HistoryMessage> findByMessageId(int messageId) {
        String sql = "SELECT * FROM history_message WHERE message_id = ? ORDER BY history_creation_date";

        logger.debug("Finding HistoryMessages by messageId: {}", messageId);

        try {
            List<HistoryMessage> results = jdbcTemplate.query(sql, HISTORY_MESSAGE_MAPPER, messageId);
            logger.info("Found {} HistoryMessages for messageId: {}", results.size(), messageId);
            return results;
        } catch (DataAccessException e) {
            logger.error("Failed to find HistoryMessages for messageId: {}. Error: {}",
                    messageId, e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public boolean update(HistoryMessage historyMessage) {
        String sql = "UPDATE history_message SET message_id = ?, content = ?, " +
                "history_creation_date = ?, update_date = ? " +
                "WHERE history_message_id = ?";

        logger.debug("Updating HistoryMessage with ID: {}", historyMessage.getHistoryMessageId());

        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    historyMessage.getMessageId(),
                    historyMessage.getContent(),
                    new Timestamp(historyMessage.getHistoryCreationDate().getTime()),
                    new Timestamp(historyMessage.getUpdateDate().getTime()),
                    historyMessage.getHistoryMessageId());

            boolean success = rowsAffected > 0;
            if (success) {
                logger.info("Successfully updated HistoryMessage with ID: {}",
                        historyMessage.getHistoryMessageId());
            } else {
                logger.warn("No HistoryMessage updated for ID: {}",
                        historyMessage.getHistoryMessageId());
            }
            return success;

        } catch (DataAccessException e) {
            logger.error("Failed to update HistoryMessage with ID: {}. Error: {}",
                    historyMessage.getHistoryMessageId(), e.getMessage());
            throw new RuntimeException("Failed to update history message", e);
        }
    }

    @Override
    public boolean deleteById(int historyMessageId) {
        String sql = "DELETE FROM history_message WHERE history_message_id = ?";

        logger.debug("Deleting HistoryMessage with ID: {}", historyMessageId);

        try {
            int rowsAffected = jdbcTemplate.update(sql, historyMessageId);
            boolean success = rowsAffected > 0;
            if (success) {
                logger.info("Successfully deleted HistoryMessage with ID: {}", historyMessageId);
            } else {
                logger.warn("No HistoryMessage deleted for ID: {}", historyMessageId);
            }
            return success;

        } catch (DataAccessException e) {
            logger.error("Failed to delete HistoryMessage with ID: {}. Error: {}",
                    historyMessageId, e.getMessage());
            throw new RuntimeException("Failed to delete history message", e);
        }
    }
}