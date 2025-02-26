package com.mau.msgboard_v4_thymeleaf.app.dataaccess;

import com.mau.msgboard_v4_thymeleaf.app.model.HistoryMessage;
import java.util.List;

public interface HistoryMessageRepository {

    // Create
    int save(HistoryMessage historyMessage);

    // Read
    HistoryMessage findById(int historyMessageId);
    List<HistoryMessage> findByMessageId(int messageId);

    // Update
    boolean update(HistoryMessage historyMessage);

    // Delete
    boolean deleteById(int historyMessageId);
}