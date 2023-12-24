package org.example;

import org.example.ChatModels.Chat;
import org.example.ChatModels.Contact;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.logging.*;

public class App {
    private final static String HOST = "localhost";
    private final static int PORT = 1400;
    private static final String DELIMITER = "#%@!&=SEPORATION=!@%#";
    private final UI ui;
    private Client client;
    private Logger logger;
    private User user;
    private HashMap<Long, Chat> chats;

    public App() {
        createLogger();
        user = loadUser();
        if (user == null)
            ui = new UI(this);
        else
            ui = new UI(this, user);
    }

    public App(String name) {
        createLogger();
        user = new User(name);
        ui = new UI(this, user);
        chats = new HashMap<>();
    }

    private void createLogger() {
        try {
            logger = Logger.getLogger(App.class.getName());
            FileHandler handler = new FileHandler("log.txt", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User loadUser() {
        try (FileInputStream fileInputStream = new FileInputStream("user.bin");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (User) objectInputStream.readObject();
        } catch (EOFException e) {
            //TODO добавить логирование
            return null;
        } catch (ClassNotFoundException | IOException e) {
            logger.log(Level.WARNING, e.getMessage());
            return null;
        }
    }

    public boolean authorization(String login) {
        try {
            Socket socket = new Socket(HOST, PORT);
            client = new Client(this, socket, login, logger);
            InetAddress inetAddress = socket.getInetAddress();
            String remoteIp = inetAddress.getHostAddress();
            logger.log(Level.INFO, "Connection\nInetAddress: " + inetAddress +
                    "\nRemote IP: " + remoteIp + "\nLocalPort:" + socket.getLocalPort());
            client.listenForMessage();
            client.sendLogin();
//          TODO добавить аутентификацию
            return true;
        } catch (IllegalArgumentException | SecurityException | IOException e) {
            logger.log(Level.WARNING, e.getMessage());
            return false;
        }
    }

    public void saveUserData(User user) {
        this.user = user;
        try (FileOutputStream fileOutputStream = new FileOutputStream("user.bin");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(user);
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void handling(String data) {
        String[] params = data.split(DELIMITER);
        String option = params[0];
        long id = Long.parseLong(params[1]);
        String payload = params[2];
        switch (option) {
            case "new_contact" -> createContact(id, payload);
            case "send_message" -> printMessage(id, payload);
        }
    }

    private void createContact(long id, String payload) {
        if (id == 0){
//            TODO контакт не найден
        }
        String[] userData = payload.split(":");
        User chatMember = new User(Long.parseLong(userData[0]), userData[1]);
        Contact contact = new Contact(id, chatMember);
        chats.put(id, contact);
        ui.createChatInChatList(contact);
    }

    public void printMessage(String message) {
        ui.print(message);
    }

    public void printMessage(long chatId, String message) {
//        TODO отправка сообщений в фоновый чат
        ui.print(message);
    }

    public void sendMessage(long chatId, String message) {
        String data = "text" + DELIMITER + chatId + DELIMITER + message;
        client.sendMessage(data);
    }

    public List<Chat> getChats() {
        return client.getChats();
    }

    public void newContact(String name) {
        String message = "find" + DELIMITER + "0" + DELIMITER + name;
        client.sendMessage(message);
    }

    public void newGroup(String name, List<String> members) {
        StringBuilder message = new StringBuilder("create" + DELIMITER + "0" + DELIMITER + name + "@@");
        for (int i = 0; i < members.size() - 1; i++) {
            message.append(members.get(i)).append("%");
        }
        message.append(members.get(members.size() - 1)).append("%");
        client.sendMessage(message.toString());
    }

    public User getUser() {
        return user;
    }
}
