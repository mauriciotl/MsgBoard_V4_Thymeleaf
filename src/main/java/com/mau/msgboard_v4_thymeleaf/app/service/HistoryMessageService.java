package com.mau.msgboard_v4_thymeleaf.app.service;

import com.mau.msgboard_v4_thymeleaf.app.model.HistoryMessage;
import java.util.List;

public interface HistoryMessageService {

    /**
     * Saves a new history message and returns its generated ID
     * @param historyMessage The message to save
     * @return The generated history message ID
     */
    int saveHistoryMessage(HistoryMessage historyMessage);

    /**
     * Retrieves a history message by its ID
     * @param historyMessageId The ID of the history message to find
     * @return The found HistoryMessage or null if not found
     */
    HistoryMessage getHistoryMessageById(int historyMessageId);

    /**
     * Retrieves all history messages for a given message ID
     * @param messageId The message ID to search for
     * @return List of HistoryMessages associated with the message ID
     */
    List<HistoryMessage> getHistoryMessagesByMessageId(int messageId);

    /**
     * Updates an existing history message
     * @param historyMessage The message with updated information
     * @return true if update was successful, false otherwise
     */
    boolean updateHistoryMessage(HistoryMessage historyMessage);

    /**
     * Deletes a history message by its ID
     * @param historyMessageId The ID of the history message to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteHistoryMessage(int historyMessageId);
}