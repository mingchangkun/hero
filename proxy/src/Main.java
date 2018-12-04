import com.chumo.marry.Person;
import com.chumo.marry.PersonImp;
import com.chumo.proxy.CMInvocationHandlerImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Main implements InvocationHandler {
    static Person person=new PersonImp();
    public static void main(String[] args) throws Throwable {
        Person instance = (Person) new CMInvocationHandlerImpl().getInstance(person);
        instance.marry();
    }

    /**
     * 动态代理生成的类调用的方法
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("我是媒婆");
        method.invoke(person,null);
        System.out.println("我去帮你找");
        return null;
    }
}
