package org.example.ChatModels;

public class Contact extends Chat{
    public Contact(long chatID, User contact) {
        super(chatID, contact.getName());
    }
}
