package org.example.packs;

import org.example.packs.annotations.*;

import java.io.*;
import java.lang.reflect.Field;
import java.net.InetAddress;

public class Envelope implements Packable, Serializable, Transportable {

//    region fields
    @Serial
    private static final long serialVersionUID = 1L;
    @LocalAddress
    private InetAddress localAddress;
    @DestinationAddress
    private InetAddress destinationAddress;
    @PostType
    private String methodType;
    @Availability
    private boolean notNull;
    @PostObjectType
    private String dataType;
    @PostObject
    private Serializable object;
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
        notNull = true;
    }

    public <T extends Serializable> Envelope(T object) {
        pack(object);
        notNull = true;
    }

    public <T extends Serializable> Envelope(InetAddress destinationAddress, String methodType, T object) {
        this.destinationAddress = destinationAddress;
        this.methodType = methodType;
        pack(object);
        notNull = true;
    }

    public <T extends Serializable> Envelope(InetAddress destinationAddress, T object) {
        this.destinationAddress = destinationAddress;
        pack(object);
        notNull = true;
    }

    public <T extends Serializable> Envelope(InetAddress localAddress, InetAddress destinationAddress, String methodType, T object) {
        this.destinationAddress = destinationAddress;
        this.localAddress = localAddress;
        this.methodType = methodType;
        pack(object);
        notNull = true;
    }

    public <T extends Serializable> Envelope(InetAddress localAddress, InetAddress destinationAddress, T object) {
        this.destinationAddress = destinationAddress;
        this.localAddress = localAddress;
        pack(object);
        notNull = true;
    }

//    endregion

//    region methods
    @Override
    @Pack
    public <T extends Serializable> void pack(T obj) {
        dataType = obj.getClass().getCanonicalName();
        object = obj;
        notNull = true;
    }

    @Override
    @Unpack
    public <T extends Serializable> T unpack() throws ClassNotFoundException {
        Class<?> clazz = Class.forName(dataType);
        return (T) clazz.cast(object);
    }

    public <T extends Serializable> boolean packedObjectIsEquals(T object) {
        return object.getClass().getCanonicalName().equals(dataType);
    }

    public Class getClassInPack() throws ClassNotFoundException {
        return Class.forName(dataType);
    }

    public Long getObjectSerialVersionUID() throws ClassNotFoundException, IllegalAccessException {
        for (Field field : Class.forName(dataType).getDeclaredFields()){
            if (field.isAnnotationPresent(Serial.class)){
                field.setAccessible(true);
                Object version = field.get(object);
                return (Long) version;
            }
        }
        return null;
    }

    public void delete() {
        notNull = false;
        object = null;
        dataType = null;
    }

    @Override
    public String toString() {
        return "Envelope{" +
                "localAddress=" + localAddress +
                ", destinationAddress=" + destinationAddress +
                ", methodType='" + methodType + '\'' +
                ", notNull=" + notNull +
                ", dataType='" + dataType + '\'' +
                ", object=" + object +
                '}';
    }

    //    endregion

//    region gettersAndSetters

    public InetAddress getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(InetAddress localAddress) {
        this.localAddress = localAddress;
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

    private void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Serializable getObject() {
        return object;
    }

    private void setObject(Serializable object) {
        this.object = object;
        notNull = true;
    }

    public InetAddress getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(InetAddress destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public boolean isNotNull() {
        return notNull;
    }

    private void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    //    endregion

}
