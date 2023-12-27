package org.example.post;

import org.example.adresses.*;
import org.example.packs.Envelope;
import org.example.packs.Transportable;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

public class Courier implements Taking {
    private UUID uuid;
    private Post post;
    private ArrayList<SenderMan> senderManList;

    public Courier(Post post) {
        this.post = post;
        senderManList = new ArrayList<>();
        uuid = UUID.randomUUID();
    }

    public Courier(Post post, ArrayList<SenderMan> senderManList) {
        this.post = post;
        this.senderManList = senderManList;
        uuid = UUID.randomUUID();
    }

    public void addSenderMan(SenderMan senderMan, int postCode) {
        senderMan.setPostCode(postCode);
        senderManList.add(senderMan);
    }

    public void addSenderMan(Object object, Method method, int postCode) {
        senderManList.add(new SenderMan(object, method, postCode));
    }

    public void addSenderMan(Object object, String methodName, int postCode) {
        senderManList.add(getSendMan(object, methodName, postCode));
    }

    public void addSenderMan(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        List<Method> list = Arrays.stream(methods).filter(it ->
                it.isAnnotationPresent(Depart.class)).toList();
        for (Method method : list) {
            Depart annotation = method.getAnnotation(Depart.class);
            int postCode = annotation.postCode();
            senderManList.add(new SenderMan(object, method, postCode));
        }
    }

    private SenderMan getSendMan(Object object, String methodName) {
        SenderMan senderMan = new SenderMan();
        for (Method method : object.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().equals(methodName)) {
                senderMan = new SenderMan(object, method);
            }
        }
        return senderMan;
    }

    private SenderMan getSendMan(Object object, String methodName, int postCode) {
        SenderMan senderMan = new SenderMan();
        for (Method method : object.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().equals(methodName)) {
                senderMan = new SenderMan(object, method, postCode);
            }
        }
        return senderMan;
    }

    @Deprecated
    public void deliver(Transportable transportable) throws ClassNotFoundException, IOException {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int postCode = getPostCode(stackTraceElements[2]);

        post.send(transportable);
    }

    @Override
    public void deliver(Serializable serializable) throws ClassNotFoundException, IOException {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int postCode = getPostCode(stackTraceElements[2]);
        Envelope envelope = new Envelope(postCode);
        post.send(envelope);
    }

    private int getPostCode(StackTraceElement stackTraceElement) throws ClassNotFoundException {
        String methodName = stackTraceElement.getMethodName();
        String className = stackTraceElement.getClassName();
        Class parentClass = Class.forName(className);
        Method parentMethod = null;
        for (Method method : parentClass.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                parentMethod = method;
            }
        }
        for (SenderMan senderMan : senderManList) {
            if (senderMan.getaClass().equals(parentClass)
                    && senderMan.getMethod().equals(parentMethod)) {
                return senderMan.getPostCode();
            }
        }
        return 0;
    }
}
