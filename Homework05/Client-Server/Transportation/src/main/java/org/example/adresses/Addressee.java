package org.example.adresses;

import org.example.packs.Packable;
import org.example.packs.Transportable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Addressee implements Addressed{
    protected Object object;
    protected Class aClass;
    protected Method method;

    public Addressee(Object object, Class aClass, Method method) {
        this.object = object;
        this.aClass = aClass;
        this.method = method;
    }

    public void setAddressee(Object object, Class aClass, Method method) {
        this.object = object;
        this.aClass = aClass;
        this.method = method;
    }

    @Override
    public void take(Transportable send) throws InvocationTargetException, IllegalAccessException {
        method.invoke(aClass.cast(object), send);
    }

    public Object getObject() {
        return object;
    }

    public Class getaClass() {
        return aClass;
    }

    public Method getMethod() {
        return method;
    }
}
