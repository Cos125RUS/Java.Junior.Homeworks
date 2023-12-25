package org.example;

import java.io.*;
import java.net.InetAddress;

public class Envelope implements Packable, Serializable, Transportable {

//    region fields
    @Serial
    private static final long serialVersionUID = 1L;
    private InetAddress inetAddress;
    private String methodType;
    private String dataType;
    private Object object;
    private transient ObjectOutputStream objectOutputStream;
    private transient OutputStream outputStream;
// endregion

//    region constructors
    public Envelope() {
    }

    public Envelope(String methodType) {
        this.methodType = methodType;
    }

    public <T extends Serializable> Envelope(String methodType, T object) {
        this.methodType = methodType;
        pack(object);
    }

    public <T extends Serializable> Envelope(InetAddress inetAddress, String methodType, T object) {
        this.inetAddress = inetAddress;
        this.methodType = methodType;
        pack(object);
    }

    public <T extends Serializable> Envelope(InetAddress inetAddress, String methodType, T object, ObjectOutputStream objectOutputStream) {
        this.inetAddress = inetAddress;
        this.methodType = methodType;
        this.objectOutputStream = objectOutputStream;
        pack(object);
    }

    public <T extends Serializable> Envelope(InetAddress inetAddress, String methodType, T object, OutputStream outputStream) throws IOException {
        this.inetAddress = inetAddress;
        this.methodType = methodType;
        this.outputStream = new ObjectOutputStream(outputStream);
        pack(object);
    }


    public <T extends Serializable> Envelope(String methodType, T object, ObjectOutputStream objectOutputStream) {
        this.methodType = methodType;
        this.objectOutputStream = objectOutputStream;
        pack(object);
    }

    public <T extends Serializable> Envelope(String methodType, T object, OutputStream outputStream) throws IOException {
        this.methodType = methodType;
        this.outputStream = new ObjectOutputStream(outputStream);
        pack(object);
    }

    public <T extends Serializable> Envelope(String methodType, ObjectOutputStream objectOutputStream) {
        this.methodType = methodType;
        this.objectOutputStream = objectOutputStream;
    }

    public <T extends Serializable> Envelope(String methodType, OutputStream outputStream) throws IOException {
        this.methodType = methodType;
        this.outputStream = new ObjectOutputStream(outputStream);
    }
//    endregion

//    region methods
    @Override
    public <T extends Serializable> void pack(T obj) {
        dataType = obj.getClass().getCanonicalName();
        object = obj;
    }

    @Override
    public <T extends Serializable> T unpack() throws ClassNotFoundException {
        Class<?> clazz = Class.forName(dataType);
        return (T) clazz.cast(object);
    }

    @Override
    public void send() throws IOException {
        objectOutputStream.writeObject(object);
    }

    public <T extends Serializable> boolean packedObjectIsEquals(T object) {
        return object.getClass().getCanonicalName().equals(dataType);
    }

    public Class getClassInPack() throws ClassNotFoundException {
        return Class.forName(dataType);
    }
//    endregion

//    region gettersAndSetters

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

//    endregion
}
