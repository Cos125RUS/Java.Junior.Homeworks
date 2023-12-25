package org.example;

public class Envelope implements Packable{
    String methodType;
    String dataType;
    Object object;

    public Envelope() {
    }

    public Envelope(String methodType) {
        this.methodType = methodType;
    }

    public <T extends Transportable> Envelope(String methodType, T object) {
        this.methodType = methodType;
        pack(object);
    }

    public <T extends Transportable> void pack(T obj) {
        dataType = obj.getClass().getCanonicalName();
        object = obj;
    }

    public <T extends Transportable> T unpack() throws ClassNotFoundException {
        Class<?> clazz = Class.forName(dataType);
        return (T) clazz.cast(object);
    }
}
