package org.example;

import org.example.ChatModels.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientManager implements Runnable {

    private final Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private User user;
    private final Logger logger;
    private final DB db;
    public final static HashMap<Long, ClientManager> activeUsers = new HashMap<>();
    private static final String DELIMITER = "#%@!&=SEPORATION=!@%#";

    public ClientManager(Socket socket, Logger logger, DB db) {
        this.socket = socket;
        this.logger = logger;
        this.db = db;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String name = bufferedReader.readLine();
//            TODO Добавить валидацию
            user = findUser(name);
            activeUsers.put(user.getId(), this);
            logger.log(Level.INFO, name + " is connected");
            if (!user.getChatsList().isEmpty()) {
//                TODO Добавить отправку чатов
                new Loader(user, db, this).start();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private User findUser(String name) {
        User findUser = db.getUserFromName(name);
        if (findUser != null) {
            logger.log(Level.INFO, name + " data find in DataBase");
            return findUser;
        }
        logger.log(Level.INFO, name + " data not find in DataBase");
        User newUser = new User(name);
        db.create(newUser);
        logger.log(Level.INFO, "Create new User. Username: " + name);
        return newUser;
    }

    @Override
    public void run() {
        String massage;
        while (socket.isConnected()) {
            try {
                massage = bufferedReader.readLine();
                if (massage == null) {
                    // для  macOS
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
                handling(massage);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                logger.log(Level.WARNING, e.getMessage());
                break;
            }
        }
    }

    private void handling(String massage) {
        String[] options = massage.split(DELIMITER);
        String contentType = options[0];
        long chatId = Long.parseLong(options[1]);
        String payloadData = options[2];
        switch (contentType) {
            case "text" -> sendMessageToContact(chatId, payloadData);
            case "group" -> sendMessageToGroup(chatId, payloadData);
            case "find" -> newContact(payloadData);
            case "create" -> newGroup(payloadData);
            case "media" -> {
            } // TODO отправка медиа
            case "contact" -> {
            } //TODO отправка контактов
            default -> logger.log(Level.WARNING, "Unknown send options");
        }
    }

    private Chat getChat(Class<? extends Chat> clazz, long chatId) {
        return db.select(clazz, chatId);
    }

    private void sendMessageToContact(long chatId, String message) {
        Chat chat = getChat(Contact.class, chatId);
        Contact contact = (Contact) chat;
        User u1 = db.select(User.class, contact.getU1ID());
        User u2 = db.select(User.class, contact.getU2ID());
        UsersList users = new UsersList();
        users.add(u1);
        users.add(u2);
        sendMessage(users, chatId, message);
    }

    private void sendMessageToGroup(long chatId, String message) {
        Chat chat = getChat(Group.class, chatId);
        Group group = (Group) chat;
        UsersList users = new UsersList();
        String[] usersList = group.getUsersList().split("%");
        for (String userId : usersList) {
            User findUser = db.select(User.class, Long.parseLong(userId));
            users.add(findUser);
        }
        sendMessage(users, chatId, message);
    }

    private void sendMessage(UsersList users, long chatId, String message) {
        String data = "send_message" + DELIMITER + chatId + DELIMITER + message;
        groupMessage(users, data);
    }

    private void newContact(String userName) {
        String data = "new_contact" + DELIMITER;
        User searchUser = db.getUserFromName(userName);
        if (searchUser != null) {
            logger.log(Level.INFO, userName + " data find in DataBase");
            Contact contact = db.getContactFromUsersId(user.getId(), searchUser.getId());
            if (contact == null) {
                contact = new Contact(user, searchUser);
                db.create(contact);
                user.addChat(contact);
                searchUser.addChat(contact);
                db.update(user);
                db.update(searchUser);
            }
            data += contact.getId() + DELIMITER;
            String dataUser1 = data + searchUser.getId() + ":" + searchUser.getName();
            send(this, dataUser1);
            String dataUser2 = data + user.getId() + ":" + user.getName();
            send(activeUsers.get(searchUser.getId()), dataUser2);
        } else {
            logger.log(Level.INFO, userName + " data not find in DataBase");
            data += "0" + DELIMITER + "-";
            send(this, data);
        }
    }

    private void newGroup(String usersList) {
        StringBuilder data = new StringBuilder("new_group" + DELIMITER);
        String[] options = usersList.split("@@");
        String groupName = options[0];
        UsersList users = (UsersList) Arrays.stream(options[1].split("%"))
                .map(db::getUserFromName)
                .toList();
        if (!users.isEmpty()) {
            Group group = new Group(groupName, users);
            db.create(group);
            data.append(group.getId()).append(DELIMITER).append(group.getName()).append("{");
            for (int i = 0; i < users.size() - 1; i++) {
                data.append(users.get(i).getId()).append(":").append(users.get(i).getName()).append(",");
            }
            data.append(users.get(users.size() - 1).getId()).append(":")
                    .append(users.get(users.size() - 1).getName()).append("}");
            logger.log(Level.INFO, "Create new group. Group ID: " + group.getId() +
                    ". Group Name: " + group.getName());
            for (User u : users) {
                ClientManager client = activeUsers.get(u.getId());
                client.send(client, data.toString());
                u.addChat(group);
            }
        } else {
            logger.log(Level.INFO, "New group don't create");
            data.append("0").append(DELIMITER).append("-");
            send(this, data.toString());
        }
    }

    private void broadcastMessage(String message) {
//        if (message.replace(user.getName() + ": ", "").charAt(0) == '@') {
//            String destination = message.split(" ")[1].replace("@", "");
//            activeUsers.stream().filter(it -> it.user.getName().equals(destination))
//                    .forEach(client -> send(client, message));
//        } else {
//            for (ClientManager client : activeUsers) {
//                if (!client.user.getName().equals(user.getName())) {
//                    send(client, message);
//                }
//            }
//        }
    }

    private void groupMessage(UsersList users, String message) {
        for (User u : users) {
            if (!user.getName().equals(u.getName())) {
                ClientManager client = activeUsers.get(u.getId());
                client.send(client, message);
            }
        }
    }

    private void send(ClientManager client, String message) {
        try {
            client.bufferedWriter.write(message);
            client.bufferedWriter.newLine();
            client.bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void send (String message) {
        send(this, message);
    }


    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        // Удаление клиента из коллекции
        removeClient();
        try {
            // Завершаем работу буфера на чтение данных
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            // Завершаем работу буфера для записи данных
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            // Закрытие соединения с клиентским сокетом
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private void removeClient() {
        activeUsers.remove(this);
        logger.log(Level.INFO, user.getName() + " is disconnected");
    }


//    region GettersAndSetters

    public User getUser() {
        return user;
    }

//    endregion
}
