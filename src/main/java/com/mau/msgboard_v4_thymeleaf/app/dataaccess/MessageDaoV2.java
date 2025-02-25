package com.mau.msgboard_v4_thymeleaf.app.dataaccess;

import com.mau.msgboard_v4_thymeleaf.app.model.Message;
import java.util.List;

public interface MessageDaoV2 {

    // Create. Return the messageId return by the DB provider.
    int save(Message message);

    // Read. Return null if message not found.
    Message findById(int messageId);
    //Return null if empty message list.
    List<Message> findAll();

    // Update. Return true if succeeded in updating.
    boolean update(Message message);

    // Delete. Return true if succeeded in deleting.
    boolean delete(int messageId);

}
