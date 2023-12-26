package org.example.adresses;

import org.example.packs.Packable;
import org.example.packs.Transportable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class PrivilegedAddressee extends Addressee{
    public PrivilegedAddressee(Object object, Class aClass, Method method) {
        super(object, aClass, method);
    }

    @Override
    public void take(Transportable send) throws InvocationTargetException, IllegalAccessException {
        takeAndOpen(send);
    }

    public void takeAndOpen(Transportable send) throws InvocationTargetException, IllegalAccessException {
        Class<?>[] interfaces = send.getClass().getInterfaces();
        if (Arrays.stream(interfaces).filter(Packable.class::equals).toList().isEmpty())
            throw new ClassCastException("Transportable class does not support Packable interface");
        Packable pack = (Packable) send;
        method.invoke(aClass.cast(object), pack);
    }
}
