package com.mau.msgboard_v4_thymeleaf.api;

import com.mau.msgboard_v4_thymeleaf.app.api.MessageController;
import com.mau.msgboard_v4_thymeleaf.app.model.Message;
import com.mau.msgboard_v4_thymeleaf.app.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageControllerTest {

    private MessageController messageController;
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        // Create mock of MessageService
        messageService = mock(MessageService.class);
        // Initialize controller with mocked service
        messageController = new MessageController(messageService);
    }

    @Test
    void createMessage_ShouldReturnCreatedStatusAndId() {
        // Arrange
        Message message = new Message();
        when(messageService.saveMessage(message)).thenReturn(1);

        // Act
        ResponseEntity<Integer> response = messageController.createMessage(message);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(messageService, times(1)).saveMessage(message);
    }

    @Test
    void getMessage_WhenMessageExists_ShouldReturnOkAndMessage() {
        // Arrange
        int messageId = 1;
        Message message = new Message();
        message.setMessageId(messageId);
        when(messageService.getMessageById(messageId)).thenReturn(message);

        // Act
        ResponseEntity<Message> response = messageController.getMessage(messageId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
        verify(messageService, times(1)).getMessageById(messageId);
    }

    @Test
    void getMessage_WhenMessageDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        int messageId = 1;
        when(messageService.getMessageById(messageId)).thenReturn(null);

        // Act
        ResponseEntity<Message> response = messageController.getMessage(messageId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(messageService, times(1)).getMessageById(messageId);
    }

    @Test
    void getAllMessages_ShouldReturnOkAndMessageList() {
        // Arrange
        List<Message> messages = Arrays.asList(new Message(), new Message());
        when(messageService.getAllMessages()).thenReturn(messages);

        // Act
        ResponseEntity<List<Message>> response = messageController.getAllMessages();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messages, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(messageService, times(1)).getAllMessages();
    }

    @Test
    void updateMessage_WhenUpdateSucceeds_ShouldReturnOk() {
        // Arrange
        int messageId = 1;
        Message message = new Message();
        when(messageService.updateMessage(any(Message.class))).thenReturn(true);

        // Act
        ResponseEntity<Void> response = messageController.updateMessage(messageId, message);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(messageService, times(1)).updateMessage(message);
        assertEquals(messageId, message.getMessageId()); // Verify ID was set
    }

    @Test
    void updateMessage_WhenUpdateFails_ShouldReturnNotFound() {
        // Arrange
        int messageId = 1;
        Message message = new Message();
        when(messageService.updateMessage(any(Message.class))).thenReturn(false);

        // Act
        ResponseEntity<Void> response = messageController.updateMessage(messageId, message);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(messageService, times(1)).updateMessage(message);
        assertEquals(messageId, message.getMessageId()); // Verify ID was set
    }

    @Test
    void deleteMessage_WhenDeleteSucceeds_ShouldReturnNoContent() {
        // Arrange
        int messageId = 1;
        when(messageService.deleteMessage(messageId)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = messageController.deleteMessage(messageId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(messageService, times(1)).deleteMessage(messageId);
    }

    @Test
    void deleteMessage_WhenDeleteFails_ShouldReturnNotFound() {
        // Arrange
        int messageId = 1;
        when(messageService.deleteMessage(messageId)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = messageController.deleteMessage(messageId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(messageService, times(1)).deleteMessage(messageId);
    }
}