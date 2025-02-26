package com.mau.msgboard_v4_thymeleaf.app.controller;

import com.mau.msgboard_v4_thymeleaf.app.model.HistoryMessage;
import com.mau.msgboard_v4_thymeleaf.app.service.HistoryMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HistoryMessageController {

    private static final Logger logger = LogManager.getLogger(HistoryMessageController.class);
    private final HistoryMessageService historyMessageService;

    @Autowired
    public HistoryMessageController(HistoryMessageService historyMessageService) {
        this.historyMessageService = historyMessageService;
    }

    @GetMapping("/history/messages/{messageId}")
    public ResponseEntity<List<HistoryMessage>> getHistoryMessagesByMessageId(@PathVariable("messageId") int messageId) {
        logger.debug("Received AJAX request to get history messages for messageId: {}", messageId);

        try {
            List<HistoryMessage> historyMessages = historyMessageService.getHistoryMessagesByMessageId(messageId);
            logger.info("Successfully retrieved {} history messages for messageId: {}",
                    historyMessages.size(), messageId);
            return ResponseEntity.ok(historyMessages);
        } catch (Exception e) {
            logger.error("Failed to retrieve history messages for messageId: {}. Error: {}",
                    messageId, e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}