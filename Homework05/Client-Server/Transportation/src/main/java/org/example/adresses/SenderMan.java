package org.example.adresses;

import java.lang.reflect.Method;

public class SenderMan {
    protected Object object;
    protected Class aClass;
    protected Method method;
    protected int postCode;

    public SenderMan() {
    }

    public SenderMan(Object object, Method method) {
        this.object = object;
        this.method = method;
        aClass = object.getClass();
    }

    public SenderMan(Object object, Method method, int postCode) {
        this.postCode = postCode;
        this.object = object;
        this.method = method;
        aClass = object.getClass();
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public Class getaClass() {
        return aClass;
    }

    public Method getMethod() {
        return method;
    }
}
