package com.chumo.proxy;

import com.chumo.marry.Person;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CMInvocationHandlerImpl implements CMInvocationHandler {
    private Person person;

    public Object getInstance(Person person) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.person=person;
        Class<? extends Person> aClass = person.getClass();
        System.out.println("被代理的对象是"+aClass);
        return CMPorxy.newProxyInstance(new CMClassLoader(),aClass.getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("我是媒婆");
        method.invoke(person,null);
        System.out.println("我帮你找媳妇");
        return null;
    }
}
