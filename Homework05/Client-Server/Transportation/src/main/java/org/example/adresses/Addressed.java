package org.example.adresses;

import org.example.packs.Transportable;

import java.lang.reflect.InvocationTargetException;

public interface Addressed {
    void take(Transportable send) throws InvocationTargetException, IllegalAccessException;
}
