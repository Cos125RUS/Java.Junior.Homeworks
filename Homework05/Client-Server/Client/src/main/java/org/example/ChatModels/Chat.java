package org.example.ChatModels;

import lombok.AllArgsConstructor;

public abstract class Chat {
    private long chatID;
    private String name;

    public Chat(long chatID, String name) {
        this.chatID = chatID;
        this.name = name;
    }

    public long getChatID() {
        return chatID;
    }

    public String getName() {
        return name;
    }
}
