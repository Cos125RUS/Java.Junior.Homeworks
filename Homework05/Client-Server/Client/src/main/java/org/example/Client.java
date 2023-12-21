package org.example;

import org.example.ChatModels.Chat;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private final App app;
    private final Socket socket;
    private final User user;
    private final Logger logger;
    private final LinkedList<Chat> chats;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;


    public Client(App app, Socket socket, String userName, Logger logger) {
        this.app = app;
        this.socket = socket;
        this.user = new User(userName);
        this.logger = logger;
        this.chats = new LinkedList<>(); //TODO Добавить загрузку чатов
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * Слушатель для входящих сообщений
     */
    public void listenForMessage() {
        new Thread(() -> {
            String message;
            while (socket.isConnected()) {
                try {
                    message = bufferedReader.readLine();
                    app.printMessage(message);
                } catch (IOException e) {
                    logger.log(Level.WARNING, e.getMessage());
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    /**
     * Отправить сообщение
     */
    public void sendMessage(String message) {
        try {
            if (socket.isConnected()) {
                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendLogin() {
        try {
            bufferedWriter.write(user.getName());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    public LinkedList<Chat> getChats() {
        return chats;
    }
}
