package org.example;

import org.example.packs.Transportable;
import org.example.packs.annotations.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Post extends Thread implements Sender, Stored {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Queue<Transportable> awaitingQueue;
    private boolean working;
    private boolean anonymity;

    public Post(InputStream inputStream) throws IOException {
        awaitingQueue = new ArrayDeque<>();
        try {
            objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            close();
            throw new IOException(e);
        }
    }

    public Post(OutputStream outputStream) throws IOException {
        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            close();
            throw new IOException(e);
        }
    }

    public Post(InputStream inputStream, OutputStream outputStream) throws IOException {
        awaitingQueue = new ArrayDeque<>();
        try {
            objectInputStream = new ObjectInputStream(inputStream);
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            close();
            throw new IOException(e);
        }
    }

    public Post(Socket socket) throws IOException {
        this.socket = socket;
        awaitingQueue = new ArrayDeque<>();
        try {
            System.out.println(socket.isConnected());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            close();
            throw new IOException(e);
        }
    }

    public Post(Socket socket, boolean anonymity) throws IOException {
        this.socket = socket;
        awaitingQueue = new ArrayDeque<>();
        this.anonymity = true;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            close();
            throw new IOException(e);
        }
    }

    public void close() throws IOException {
        working = false;
        if (objectOutputStream != null) objectOutputStream.close();
        if (objectInputStream != null) objectInputStream.close();
        if (socket != null) socket.close();
    }

    @Override
    public void send(Transportable transportable) throws IOException {
        if (!anonymity) {
            try {
                stamp(transportable);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            objectOutputStream.writeObject(transportable);
        } else {
            objectOutputStream.writeObject(transportable);
        }
    }

    private void stamp(Transportable transportable) throws IllegalAccessException {
        Arrays.stream(transportable.getClass().getDeclaredFields()).filter(field -> {
            field.setAccessible(true);
            return !Arrays.stream(field.getAnnotations()).filter(
                            annotation -> annotation.annotationType().equals(Address.class))
                    .toList().isEmpty();
        }).toList().get(0).set(transportable, socket.getInetAddress());
    }

    @Override
    public <T extends Transportable> T get() {
        return (T) awaitingQueue.peek();
    }

    public <T extends Transportable> T[] getAll() {
        return (T[]) awaitingQueue.toArray();
    }

    @Override
    public void run() {
        working = true;
        while (working) {
            try {
                Transportable transportable = (Transportable) objectInputStream.readObject();
                awaitingQueue.add(transportable);
            } catch (ClassNotFoundException | IOException e) {
                try {
                    close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }
        }
    }
}
