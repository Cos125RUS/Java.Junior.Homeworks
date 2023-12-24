package org.example;

import org.example.ChatModels.Chat;
import org.example.ChatModels.Contact;
import org.example.ChatModels.Group;
import org.example.ChatModels.User;
import org.hibernate.UnknownEntityTypeException;

import java.io.BufferedWriter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Loader extends Thread {
    private final User user;
    private final DB db;
    private final ClientManager client;
    private static final String DELIMITER = "#%@!&=SEPORATION=!@%#";

    public Loader(User user, DB db, ClientManager client) {
        this.user = user;
        this.db = db;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Integer> chatsId = Arrays.stream(user.getChatsList().split("%"))
                .map(Integer::parseInt).toList();
        for (int id : chatsId) {
            try {
                Contact contact = db.select(Contact.class, id);
                String data = "new_contact" + DELIMITER + contact.getId() + DELIMITER;
                long memberId;
                if (user.getId() == contact.getU1ID())
                    memberId = contact.getU2ID();
                else memberId = contact.getU1ID();
                User member = db.select(User.class, memberId);
                data += member.getId() + ":" + member.getName();
                client.send(data); //TODO Доработать приём на стороне клиента
            } catch (UnknownEntityTypeException e) {
                Group group = db.select(Group.class, id);
                String data = "new_group" + DELIMITER + group.getId() + DELIMITER;
//                TODO Добавить отправку групп
            }
        }

    }
}
