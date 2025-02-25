package com.mau.msgboard_v4_thymeleaf.app.model;
import java.util.Date;

public class HistoryMessage {
    private int historyMessageId;
    private int messageId;
    private String content;
    private Date historyCreationDate;
    private Date updateDate;

    // Constructor with all fields
    public HistoryMessage(int historyMessageId, int messageId, String content, Date historyCreationDate, Date updateDate) {
        this.historyMessageId = historyMessageId;
        this.messageId = messageId;
        this.content = content;
        this.historyCreationDate = historyCreationDate;
        this.updateDate = updateDate;
    }

    // Getters and Setters (Existing code remains here)
    public int getHistoryMessageId() {
        return historyMessageId;
    }

    public void setHistoryMessageId(int historyMessageId) {
        this.historyMessageId = historyMessageId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getHistoryCreationDate() {
        return historyCreationDate;
    }

    public void setHistoryCreationDate(Date historyCreationDate) {
        this.historyCreationDate = historyCreationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
