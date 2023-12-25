package org.example;

public interface Packable {
    <T extends Transportable> void pack(T obj);
    <T extends Transportable> T unpack() throws ClassNotFoundException;
}
