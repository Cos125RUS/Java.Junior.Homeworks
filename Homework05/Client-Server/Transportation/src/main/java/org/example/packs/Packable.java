package org.example.packs;

import java.io.Serializable;

public interface Packable {
    <T extends Serializable> void pack(T obj);
    <T extends Serializable> T unpack() throws ClassNotFoundException;
}
