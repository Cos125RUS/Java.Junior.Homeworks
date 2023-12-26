package org.example;

import com.fasterxml.jackson.databind.introspect.Annotated;
import org.example.adresses.Addressed;
import org.example.adresses.Addressee;
import org.example.adresses.PrivilegedAddressee;
import org.example.packs.Packable;
import org.example.packs.Transportable;
import org.example.packs.annotations.*;
import org.example.packs.annotations.PostCode;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

public class Post extends Thread implements Sender, Stored {
    //    region fields
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Queue<Transportable> awaitingQueue;
    private Addressed addressed;
    private HashMap<Integer, Addressed> addressedMap;
    private boolean working;
    private boolean anonymity;
//    endregion

    //    region constructions
    public Post(InputStream inputStream) throws IOException {
        awaitingQueue = new ArrayDeque<>();
        addressedMap = new HashMap<>();
        try {
            objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            close();
            throw new IOException(e);
        }
    }

    public Post(OutputStream outputStream) throws IOException {
        addressedMap = new HashMap<>();
        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            close();
            throw new IOException(e);
        }
    }

    public Post(InputStream inputStream, OutputStream outputStream) throws IOException {
        awaitingQueue = new ArrayDeque<>();
        addressedMap = new HashMap<>();
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
        addressedMap = new HashMap<>();
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
        addressedMap = new HashMap<>();
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            close();
            throw new IOException(e);
        }
    }
//    endregion

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

    public void setAddressee(Object object, Method method) {
        addressed = new Addressee(object, object.getClass(), method);
    }

    public void setAddressee(Object object, String method) throws NoSuchMethodException {
        addressed = new Addressee(object, object.getClass(),
                object.getClass().getMethod(method));
    }

    public void setAddressee(Object object, Method method, boolean unpacking) {
        addressed = new PrivilegedAddressee(object, object.getClass(), method);
    }

    public void setAddressee(Object object, String method, boolean unpacking) throws NoSuchMethodException {
        addressed = new PrivilegedAddressee(object, object.getClass(),
                object.getClass().getMethod(method));
    }


    public void addAddressee(Object object, Method method, int postCode) {
        addressed = new PrivilegedAddressee(object, object.getClass(), method);
        addressedMap.put(postCode, addressed);
    }

    public void addAddressee(Object object, String method, int postCode) throws NoSuchMethodException {
        addressed = new PrivilegedAddressee(object, object.getClass(),
                object.getClass().getMethod(method));
        addressedMap.put(postCode, addressed);
    }

    public void addAddressee(HashMap<Integer, Addressed> addressedMap, int postCode) throws NoSuchMethodException {
        this.addressedMap.putAll(addressedMap);
    }

    private void redirection(Transportable transportable) throws InvocationTargetException, IllegalAccessException {
        if (addressed != null)
            addressed.take(transportable);
        if (!addressedMap.isEmpty())
            redirectionToAll(transportable);
    }

    private void redirectionToAll(Transportable transportable) throws InvocationTargetException, IllegalAccessException {
        Field[] declaredFields = transportable.getClass().getDeclaredFields();
        int id = Arrays.stream(declaredFields).filter(
                it -> !Arrays.stream(it.getAnnotations())
                        .filter(PostCode.class::equals).toList().isEmpty())
                .toList().get(0).getInt(transportable);
        addressedMap.get(id).take(transportable);
    }

    public void stopRedirection() {
        addressed = null;
    }

    public void stopRedirectionAll() {
        addressed = null;
        addressedMap = null;
    }

    @Override
    public void run() {
        working = true;
        while (working) {
            try {
                Transportable transportable = (Transportable) objectInputStream.readObject();
                if (addressed == null && addressedMap.isEmpty())
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

    public Addressed getAddressed() {
        return addressed;
    }

    public HashMap<Integer, Addressed> getAddressedMap() {
        return addressedMap;
    }

    public List<Addressed> getAddressedList() {
        if (!addressedMap.isEmpty())
            return (ArrayList<Addressed>) addressedMap.values();
        else
            return null;
    }
}
