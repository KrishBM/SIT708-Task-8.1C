package com.example.task81;

public class Message {
    public static final int TYPE_USER = 1;
    public static final int TYPE_BOT = 2;

    private String content;
    private int type;

    public Message(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
} 