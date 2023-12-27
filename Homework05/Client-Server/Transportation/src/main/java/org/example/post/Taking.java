package org.example.post;

import org.example.packs.Transportable;

import java.io.IOException;
import java.io.Serializable;

public interface Taking {
    void deliver(Serializable serializable) throws ClassNotFoundException, NoSuchMethodException, IOException;
}
