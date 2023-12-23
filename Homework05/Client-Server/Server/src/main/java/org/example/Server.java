package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server {
    private final ServerSocket serverSocket;
    private Logger logger;
    private final DB db;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        createLogger();
        db = new DB(logger);
    }

    private void createLogger() {
        try {
            logger = Logger.getLogger(Server.class.getName());
            FileHandler handler = new FileHandler("ServerLog.txt", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runServer() {
        logger.log(Level.INFO, "Server started");
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientManager clientManager = new ClientManager(socket, logger, db);
                logger.log(Level.INFO, "New connection with IP: "
                        + socket.getInetAddress().getHostAddress());
                Thread thread = new Thread(clientManager);
                thread.start();
            }
        } catch (IOException e) {
            closeSocket();
            db.exit();
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private void closeSocket() {
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

}
