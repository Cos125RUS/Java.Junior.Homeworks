package org.example.post;

import org.example.adresses.Addressed;
import org.example.adresses.Addressee;
import org.example.adresses.Addressing;
import org.example.adresses.PrivilegedAddressee;
import org.example.packs.Transportable;
import org.example.packs.annotations.*;
import org.example.packs.annotations.PostCode;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Post extends Thread implements Sender, Stored {
    //    region fields
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Queue<Transportable> awaitingQueue;
    private HashMap<Integer, PostBox> postBoxes;
    private ArrayList<Postman> postmenList;
    private boolean working;
    private boolean anonymity;
    private boolean redirected;
//    endregion

    //    region constructions
    public Post(InputStream inputStream) throws IOException {
        awaitingQueue = new ArrayDeque<>();
        postBoxes = new HashMap<>();
        postmenList = new ArrayList<>();
        try {
            objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            close();
            throw new IOException(e);
        }
    }

    public Post(OutputStream outputStream) throws IOException {
        postBoxes = new HashMap<>();
        postmenList = new ArrayList<>();
        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            close();
            throw new IOException(e);
        }
    }

    public Post(InputStream inputStream, OutputStream outputStream) throws IOException {
        awaitingQueue = new ArrayDeque<>();
        postBoxes = new HashMap<>();
        postmenList = new ArrayList<>();
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
        postBoxes = new HashMap<>();
        postmenList = new ArrayList<>();
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
        this.anonymity = true;
        awaitingQueue = new ArrayDeque<>();
        postBoxes = new HashMap<>();
        postmenList = new ArrayList<>();
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            close();
            throw new IOException(e);
        }
    }
//    endregion

    //    region methods
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

    private void stamp(Transportable transportable) throws IllegalAccessException, UnknownHostException {
        Arrays.stream(transportable.getClass().getDeclaredFields()).filter(field -> {
            field.setAccessible(true);
            return !Arrays.stream(field.getAnnotations()).filter(
                            annotation -> annotation.annotationType().equals(LocalAddress.class))
                    .toList().isEmpty();
        }).toList().get(0).set(transportable, InetAddress.getLocalHost());
    }

    @Override
    public <T extends Transportable> T get() {
        return (T) awaitingQueue.peek();
    }

    public <T extends Transportable> T[] getAll() {
        return (T[]) awaitingQueue.toArray();
    }

    public PostBox getPostBox(int postCode) {
        PostBox postBox = new PostBox(postCode);
        postBoxes.put(postCode, postBox);
        redirected = true;
        return postBox;
    }

    public Postman getPostman() {
        Postman postman = new Postman();
        redirected = true;
        return postman;
    }

    private void redirection(Transportable transportable) throws InvocationTargetException, IllegalAccessException {
        boolean destinationFound = false;
        for (Postman postman : postmenList) {
            if (postman.newCorespondent(transportable))
                destinationFound = true;
        }
        if (!destinationFound) {
            Integer postCode;
            if ((postCode = Postman.getPostCode(transportable)) != null) {
                postBoxes.get(postCode).add(transportable);
            }
        } else awaitingQueue.add(transportable);
    }

    public void stopRedirection() {
        redirected = false;
    }

    public boolean startRedirection() {
        if (!postBoxes.isEmpty() || !postmenList.isEmpty()) {
            return (redirected = true);
        } else return false;
    }

    public void clearRedirections() {
        postBoxes = new HashMap<>();
        postmenList = new ArrayList<>();
        redirected = false;
    }

    public void deleteRedirect(PostBox postBox) {
        postBoxes.remove(postBox.getPostCode());
        if (postBoxes.isEmpty() && postmenList.isEmpty())
            redirected = false;
    }

    public void deleteRedirect(Postman postman) {
        postmenList.remove(postman);
        if (postBoxes.isEmpty() && postmenList.isEmpty())
            redirected = false;
    }

    @Override
    public void run() {
        working = true;
        while (working) {
            try {
                Transportable transportable = (Transportable) objectInputStream.readObject();
                if (!redirected)
                    awaitingQueue.add(transportable);
                else
                    redirection(transportable);
            } catch (IOException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
                try {
                    close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isRedirected() {
        return redirected;
    }

    //    endregion
}
