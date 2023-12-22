package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.UnknownHostException;

public class Program {
    private static final int PORT = 1400;

    public static void main(String[] args) {
        try
        {
            ServerSocket serverSocket = new ServerSocket(PORT);
            Server server = new Server(serverSocket);
            server.runServer();
        }
        catch (UnknownHostException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
