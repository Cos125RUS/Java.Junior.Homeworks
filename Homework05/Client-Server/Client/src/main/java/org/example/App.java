package org.example;

import org.example.ChatModels.Chat;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class App {
    private final static String HOST = "localhost";
    private final static int PORT = 1400;
    private final UI ui;
    private Client client;
    private Logger logger;

    public App() {
        try {
            logger = Logger.getLogger(App.class.getName());
            FileHandler handler = new FileHandler("log.txt", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        TODO добавить загрузку данных пользователя
        User user = loadUser();
        if (user == null)
            ui = new UI(this);
        else
            ui = new UI(this, user);
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

    public void printMessage(String message) {
        ui.print(message);
    }

    public void sendMessage(String message) {
        client.sendMessage(message);
    }

    public List<Chat> getChats() {
        return client.getChats();
    }

}
