package org.example.adresses;

import org.example.packs.Transportable;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public interface Addressed {
    void take(Transportable object) throws InvocationTargetException, IllegalAccessException;
}
