package com.example.gamevault.ui.social;

public class Message {
    private String userId;
    private String message;
    private long timestamp;

    public Message(String content) {

    }

    public Message(String userId, String message, long timestamp) {
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }
    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
