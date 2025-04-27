package com.mau.msgboard_v4_thymeleaf.app.service;

import com.mau.msgboard_v4_thymeleaf.app.dataaccess.MessageDaoV2;
import com.mau.msgboard_v4_thymeleaf.app.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class MessageServiceMysql implements MessageService {

    private static final Logger logger = LogManager.getLogger(MessageServiceMysql.class);

    private final MessageDaoV2 messageDao;

    @Autowired
    public MessageServiceMysql(MessageDaoV2 messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    @Transactional
    public int saveMessage(Message message) {
        try {
            logger.info("Saving new message: {}", message);
            // Set creationDate if null
            if (message.getCreationDate() == null) {
                message.setCreationDate(Timestamp.from(Instant.now()));
                logger.debug("Set creationDate to: {}", message.getCreationDate());
            }
            return messageDao.save(message);
        } catch (DataAccessException e) {
            logger.error("Database error while saving message: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving message", e);
        } catch (Exception e) {
            logger.error("Unexpected error while saving message: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while saving message", e);
        }
    }

    @Override
    public Message getMessageById(int messageId) {
        try {
            logger.info("Fetching message with ID: {}", messageId);
            return messageDao.findById(messageId);
        } catch (DataAccessException e) {
            logger.error("Database error while fetching message with ID {}: {}", messageId, e.getMessage(), e);
            throw new RuntimeException("Error fetching message", e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching message: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while fetching message", e);
        }
    }

    @Override
    public List<Message> getAllMessages() {
        try {
            logger.info("Fetching all messages");
            return messageDao.findAll();
        } catch (DataAccessException e) {
            logger.error("Database error while fetching messages: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching messages", e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching messages: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while fetching messages", e);
        }
    }

    @Override
    @Transactional
    public boolean updateMessage(Message message) {
        try {
            logger.info("Updating message with ID: {}", message.getMessageId());
            logger.info("The HistoryMessage object is created by a Trigger on the DB");
            //NOTE. The HistoryMessage object is created by a Trigger on the DB
            // Set creationDate if null
            if (message.getCreationDate() == null) {
                message.setCreationDate(Timestamp.from(Instant.now()));
                logger.debug("Set creationDate for updateMessage to: {}", message.getCreationDate());
            }
            return messageDao.update(message);
        } catch (DataAccessException e) {
            logger.error("Database error while updating message with ID {}: {}", message.getMessageId(), e.getMessage(), e);
            throw new RuntimeException("Error updating message", e);
        } catch (Exception e) {
            logger.error("Unexpected error while updating message: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while updating message", e);
        }
    }

    @Override
    @Transactional
    public boolean deleteMessage(int messageId) {
        try {
            logger.info("Deleting message with ID: {}", messageId);
            return messageDao.delete(messageId);
        } catch (DataAccessException e) {
            logger.error("Database error while deleting message with ID {}: {}", messageId, e.getMessage(), e);
            throw new RuntimeException("Error deleting message", e);
        } catch (Exception e) {
            logger.error("Unexpected error while deleting message: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while deleting message", e);
        }
    }
}