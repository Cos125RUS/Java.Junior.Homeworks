package org.example;

import org.example.ChatModels.Chat;
import org.example.ChatModels.User;

import java.io.BufferedWriter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Loader extends Thread {
    private final User user;
    private final DB db;
    private final Logger logger;
    private final BufferedWriter bufferedWriter;

    public Loader(User user, DB db, Logger logger, BufferedWriter bufferedWriter) {
        this.user = user;
        this.db = db;
        this.logger = logger;
        this.bufferedWriter = bufferedWriter;
    }

    @Override
    public void run() {
        Arrays.stream(user.getChatsList().split("%"))
                .map(Integer::parseInt)
                .forEach(id -> {
                    Chat chat = db.select(Chat.class, id);
                    System.out.println(chat);
//                  TODO Добавить отправку чатов
                });

    }
}
