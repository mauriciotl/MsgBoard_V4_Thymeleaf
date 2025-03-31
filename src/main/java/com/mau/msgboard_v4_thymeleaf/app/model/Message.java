package com.mau.msgboard_v4_thymeleaf.app.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

public class Message {

    private int messageId;

    // Set server-side from auth, no client input; that's why validations are not necessary here.
    private int userId;

    @NotBlank(message="Content is required")
    @Size(min=3, message="Content must be at least 3 characters long")
    private String content;

    private Timestamp creationDate;

    // Empty constructor to be able to validate on the view
    public Message() {
    }

    // Constructor without messageId as it will use when the DB implementation provides it
    public Message(int userId, String content, Timestamp creationDate) {
        this.userId = userId;
        this.content = content;
        this.creationDate = creationDate;
    }

    public Message(int messageId, int userId, String content, Timestamp creationDate) {
        this.messageId = messageId;
        this.userId = userId;
        this.content = content;
        this.creationDate = creationDate;
    }

    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}