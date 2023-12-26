package org.example.post;

import org.example.packs.Transportable;

import java.lang.reflect.InvocationTargetException;

public interface Delivering {
    void newCorespondent(Transportable transportable) throws IllegalAccessException, InvocationTargetException;
}
