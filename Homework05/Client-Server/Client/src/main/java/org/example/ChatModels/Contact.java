package org.example.ChatModels;

import lombok.AllArgsConstructor;
import org.example.User;

public class Contact extends Chat{
    public Contact(long chatID, User contact) {
        super(chatID, contact.getName());
    }
}
