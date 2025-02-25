package com.mau.msgboard_v4_thymeleaf.app.service;

import com.mau.msgboard_v4_thymeleaf.app.model.Message;
import java.util.List;

public interface MessageService {

    int saveMessage(Message message);

    Message getMessageById(int messageId);

    List<Message> getAllMessages();

    boolean updateMessage(Message message);

    boolean deleteMessage(int messageId);
}
