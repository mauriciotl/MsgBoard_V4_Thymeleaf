package com.mau.msgboard_v4_thymeleaf.app.service;

import com.mau.msgboard_v4_thymeleaf.app.dataaccess.HistoryMessageRepository;
import com.mau.msgboard_v4_thymeleaf.app.model.HistoryMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryMessageServiceMysql implements HistoryMessageService {

    private static final Logger logger = LogManager.getLogger(HistoryMessageServiceMysql.class);
    private final HistoryMessageRepository historyMessageRepository;

    @Autowired
    public HistoryMessageServiceMysql(HistoryMessageRepository historyMessageRepository) {
        this.historyMessageRepository = historyMessageRepository;
    }

    @Override
    public int saveHistoryMessage(HistoryMessage historyMessage) {
        logger.debug("Saving history message for messageId: {}", historyMessage.getMessageId());

        if (historyMessage == null) {
            logger.error("Attempted to save null HistoryMessage");
            throw new IllegalArgumentException("HistoryMessage cannot be null");
        }

        try {
            int id = historyMessageRepository.save(historyMessage);
            logger.info("Successfully saved history message with ID: {}", id);
            return id;
        } catch (Exception e) {
            logger.error("Failed to save history message: {}", e.getMessage());
            throw e; // Re-throw to maintain the repository's exception handling
        }
    }

    @Override
    public HistoryMessage getHistoryMessageById(int historyMessageId) {
        logger.debug("Retrieving history message with ID: {}", historyMessageId);

        if (historyMessageId <= 0) {
            logger.warn("Invalid history message ID: {}", historyMessageId);
            return null;
        }

        try {
            HistoryMessage message = historyMessageRepository.findById(historyMessageId);
            if (message != null) {
                logger.info("Found history message with ID: {}", historyMessageId);
            } else {
                logger.debug("No history message found with ID: {}", historyMessageId);
            }
            return message;
        } catch (Exception e) {
            logger.error("Error retrieving history message with ID: {}: {}",
                    historyMessageId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<HistoryMessage> getHistoryMessagesByMessageId(int messageId) {
        logger.debug("Retrieving history messages for messageId: {}", messageId);

        if (messageId <= 0) {
            logger.warn("Invalid message ID: {}", messageId);
            return List.of(); // Return empty list for invalid input
        }

        try {
            List<HistoryMessage> messages = historyMessageRepository.findByMessageId(messageId);
            logger.info("Found {} history messages for messageId: {}", messages.size(), messageId);
            return messages;
        } catch (Exception e) {
            logger.error("Error retrieving history messages for messageId: {}: {}",
                    messageId, e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean updateHistoryMessage(HistoryMessage historyMessage) {
        logger.debug("Updating history message with ID: {}", historyMessage.getHistoryMessageId());

        if (historyMessage.getHistoryMessageId() <= 0) {
            logger.error("Invalid history message ID for update: {}",
                    historyMessage.getHistoryMessageId());
            throw new IllegalArgumentException("HistoryMessage ID must be positive");
        }

        try {
            boolean success = historyMessageRepository.update(historyMessage);
            if (success) {
                logger.info("Successfully updated history message with ID: {}",
                        historyMessage.getHistoryMessageId());
            } else {
                logger.warn("No history message updated for ID: {}",
                        historyMessage.getHistoryMessageId());
            }
            return success;
        } catch (Exception e) {
            logger.error("Failed to update history message with ID: {}: {}",
                    historyMessage.getHistoryMessageId(), e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean deleteHistoryMessage(int historyMessageId) {
        logger.debug("Deleting history message with ID: {}", historyMessageId);

        if (historyMessageId <= 0) {
            logger.warn("Invalid history message ID for deletion: {}", historyMessageId);
            return false;
        }

        try {
            boolean success = historyMessageRepository.deleteById(historyMessageId);
            if (success) {
                logger.info("Successfully deleted history message with ID: {}", historyMessageId);
            } else {
                logger.warn("No history message deleted for ID: {}", historyMessageId);
            }
            return success;
        } catch (Exception e) {
            logger.error("Failed to delete history message with ID: {}: {}",
                    historyMessageId, e.getMessage());
            throw e;
        }
    }
}