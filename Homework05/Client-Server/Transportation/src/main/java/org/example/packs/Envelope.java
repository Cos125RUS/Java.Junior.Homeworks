package org.example.packs;

import org.example.packs.annotations.*;

import java.io.*;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.UUID;

public class Envelope implements Packable, Serializable, Transportable {

//    region fields
    @Serial
    private static final long serialVersionUID = 1L;
    @PackId
    private UUID uuid;
    @LocalAddress
    private InetAddress localAddress;
    @DestinationAddress
    private InetAddress destinationAddress;
    @PostType
    private String postType;
    @PostCode
    private int postCode;
    @Availability
    private boolean notNull;
    @PostObjectType
    private String dataType;
    @PostObject
    private Serializable object;
// endregion

//    region constructors
    public Envelope() {
        uuid = UUID.randomUUID();
    }

    public Envelope(String postType) {
        this.postType = postType;
        uuid = UUID.randomUUID();
    }


    public Envelope(int postCode) {
        this.postCode = postCode;
        uuid = UUID.randomUUID();
    }

    public <T extends Serializable> Envelope(T object, String postType) {
        this.postType = postType;
        uuid = UUID.randomUUID();
        pack(object);
        notNull = true;
    }


    public <T extends Serializable> Envelope(T object, int postCode) {
        this.postCode = postCode;
        uuid = UUID.randomUUID();
        pack(object);
        notNull = true;
    }

    public <T extends Serializable> Envelope(T object) {
        uuid = UUID.randomUUID();
        pack(object);
        notNull = true;
    }

    public <T extends Serializable> Envelope(T object, InetAddress destinationAddress, String postType) {
        this.destinationAddress = destinationAddress;
        this.postType = postType;
        uuid = UUID.randomUUID();
        pack(object);
        notNull = true;
    }


    public <T extends Serializable> Envelope(T object, int postCode, InetAddress destinationAddress) {
        this.destinationAddress = destinationAddress;
        this.postCode = postCode;
        uuid = UUID.randomUUID();
        pack(object);
        notNull = true;
    }

    public <T extends Serializable> Envelope(T object, InetAddress destinationAddress) {
        this.destinationAddress = destinationAddress;
        uuid = UUID.randomUUID();
        pack(object);
        notNull = true;
    }

    public <T extends Serializable> Envelope(T object, InetAddress localAddress, InetAddress destinationAddress, String postType) {
        this.destinationAddress = destinationAddress;
        this.localAddress = localAddress;
        this.postType = postType;
        uuid = UUID.randomUUID();
        pack(object);
        notNull = true;
    }


    public <T extends Serializable> Envelope(T object, int postCode, InetAddress localAddress, InetAddress destinationAddress) {
        this.destinationAddress = destinationAddress;
        this.localAddress = localAddress;
        this.postCode = postCode;
        uuid = UUID.randomUUID();
        pack(object);
        notNull = true;
    }

    public <T extends Serializable> Envelope(T object, InetAddress localAddress, InetAddress destinationAddress) {
        this.destinationAddress = destinationAddress;
        this.localAddress = localAddress;
        uuid = UUID.randomUUID();
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
                ", methodType='" + postType + '\'' +
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

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getDataType() {
        return dataType;
    }

    private void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
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

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    //    endregion

}
