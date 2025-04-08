package com.mau.msgboard_v4_thymeleaf.app.api;

import com.mau.msgboard_v4_thymeleaf.app.model.Message;
import com.mau.msgboard_v4_thymeleaf.app.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Create a new message
    @PostMapping
    public ResponseEntity<Integer> createMessage(@RequestBody Message message) {
        int messageId = messageService.saveMessage(message);
        return new ResponseEntity<>(messageId, HttpStatus.CREATED);
    }

    // Get a message by ID
    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable("id") int messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get all messages
    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // Update a message
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMessage(@PathVariable("id") int messageId,
                                              @RequestBody Message message) {
        message.setMessageId(messageId); // Ensure the ID matches the path variable
        boolean updated = messageService.updateMessage(message);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete a message
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") int messageId) {
        boolean deleted = messageService.deleteMessage(messageId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}