package org.example.ChatModels;

import org.example.User;

import java.util.List;

public class Group extends Chat{
    private List<User> users;
    public Group(long chatID, String name, List<User> users) {
        super(chatID, name);
        this.users = users;
    }
}
