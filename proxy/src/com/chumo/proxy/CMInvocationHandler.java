package com.chumo.proxy;

import com.chumo.marry.Person;

import java.lang.reflect.Method;

public interface CMInvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable;
}
