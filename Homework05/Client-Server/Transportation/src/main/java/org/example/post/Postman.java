package org.example.post;

import org.example.adresses.Addressed;
import org.example.adresses.Addressee;
import org.example.adresses.Addressing;
import org.example.adresses.PrivilegedAddressee;
import org.example.packs.Transportable;
import org.example.packs.annotations.PostCode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Postman implements Delivering{
    private UUID uuid;
    private Addressed defaultAddressed;
    private final HashMap<Integer, Addressed> addressedMap;

    public Postman() {
        uuid = UUID.randomUUID();
        addressedMap = new HashMap<>();
    }

    public Postman(Addressed defaultAddressed) {
        this.defaultAddressed = defaultAddressed;
        uuid = UUID.randomUUID();
        addressedMap = new HashMap<>();
    }

    public Postman(HashMap<Integer, Addressed> addressedMap) {
        this.addressedMap = addressedMap;
        uuid = UUID.randomUUID();
    }

    public void setAddressee(Object object, Method method) {
        defaultAddressed = new Addressee(object, object.getClass(), method);
    }

    public void setAddressee(Object object, String method) throws NoSuchMethodException {
        defaultAddressed = new Addressee(object, object.getClass(),
                object.getClass().getMethod(method));
    }

    public void setAddressee(Object object, Method method, boolean unpacking) {
        if (unpacking)
            defaultAddressed = new PrivilegedAddressee(object, object.getClass(), method);
        else
            defaultAddressed = new Addressee(object, object.getClass(), method);
    }

    public void setAddressee(Object object, String method, boolean unpacking) throws NoSuchMethodException {
        if (unpacking)
            defaultAddressed = new PrivilegedAddressee(object, object.getClass(),
                    object.getClass().getMethod(method));
        else
            defaultAddressed = new Addressee(object, object.getClass(),
                    object.getClass().getMethod(method));
    }

    public void addAddressee(Object object, Method method, int postCode) {
        Addressed addressed = new Addressee(object, object.getClass(), method);
        addressedMap.put(postCode, addressed);
    }

    public void addAddressee(Object object, String method, int postCode) throws NoSuchMethodException {
        Addressed addressed = new Addressee(object, object.getClass(),
                object.getClass().getMethod(method));
        addressedMap.put(postCode, addressed);
    }

    public void addAddressee(Object object, Method method, int postCode, boolean unpacking) {
        Addressed addressed;
        if (unpacking) {
            addressed = new PrivilegedAddressee(object, object.getClass(), method);
        } else {
            addressed = new Addressee(object, object.getClass(), method);
        }
        addressedMap.put(postCode, addressed);
    }

    public void addAddressee(Object object, String method, int postCode, boolean unpacking) throws NoSuchMethodException {
        if (unpacking) {
            Addressed addressed = new PrivilegedAddressee(object, object.getClass(),
                    object.getClass().getMethod(method));
        } else {
            Addressed addressed = new Addressee(object, object.getClass(),
                    object.getClass().getMethod(method));
        }
        addressedMap.put(postCode, defaultAddressed);
    }

    public void addAddressee(HashMap<Integer, Addressed> addressedMap) throws NoSuchMethodException {
        this.addressedMap.putAll(addressedMap);
    }

    public void addAddressee(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        List<Method> list = Arrays.stream(methods).filter(it ->
                it.isAnnotationPresent(Addressing.class)).toList();
        for (Method method : list) {
            Addressing annotation = method.getAnnotation(Addressing.class);
            int postCode = annotation.postCode();
            addressedMap.put(postCode, new Addressee(object, object.getClass(), method));
        }
    }

    public void addAddressee(Object object, boolean unpacked) {
        if (unpacked) {
            Method[] methods = object.getClass().getDeclaredMethods();
            List<Method> list = Arrays.stream(methods).filter(it ->
                    it.isAnnotationPresent(Addressing.class)).toList();
            for (Method method : list) {
                Addressing annotation = method.getAnnotation(Addressing.class);
                int postCode = annotation.postCode();
                addressedMap.put(postCode, new PrivilegedAddressee(object, object.getClass(), method));
            }
        } else addAddressee(object);
    }

    @Override
    public void newCorespondent(Transportable transportable) throws IllegalAccessException, InvocationTargetException {
        Field[] declaredFields = transportable.getClass().getDeclaredFields();
        List<Field> list = Arrays.stream(declaredFields).filter(it ->
                it.isAnnotationPresent(PostCode.class)).toList();
        if (list.isEmpty()) {
            if (defaultAddressed == null)
                throw new RuntimeException(
                        "PostCode annotation not found and defaultAddressed is null");
            else defaultAddressed.take(transportable);
        } else {
            int id = list.get(0).getInt(transportable);
            addressedMap.get(id).take(transportable);
        }
    }
}
