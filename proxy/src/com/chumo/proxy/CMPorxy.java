package com.chumo.proxy;

import sun.reflect.Reflection;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 生成代理对象的代码
 */
public class CMPorxy {
    private static String ln="\r\n";
    public static Object newProxyInstance(CMClassLoader loader,
                                          Class<?>[] interfaces,
                                          CMInvocationHandler h)
            throws IllegalArgumentException, IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //1、生成源代码
        String proxySrc=gengerateSrc(interfaces);
        //2、将生成的源代码输出到磁盘，保存为.java文件
        String fileNmae=CMPorxy.class.getResource("").getPath();
        File f=new File(fileNmae+"$Proxy0.java");
        try {
            FileWriter fw=new FileWriter(f);
            fw.write(proxySrc);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //3、编译源代码，并生成.class文件
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> objects = manager.getJavaFileObjects(f);

        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, objects);
        task.call();
        manager.close();
        //4、将.class文件中的内容，动态加载到JVM中来
        Class<?> $Proxy0 = loader.findClass("$Proxy0");//获得类
        //5、返回被代理的对象
        Constructor<?> constructor = $Proxy0.getConstructor(CMInvocationHandler.class);//获取构造方法
        f.delete();//删除java文件
        return constructor.newInstance(h);//实例化
    }

    private static String gengerateSrc(Class<?>[] interfaces){
        StringBuffer src=new StringBuffer();
        src.append("package com.chumo.proxy;"+ln);
        src.append("import java.lang.reflect.InvocationHandler;\n" +ln+
                "import java.lang.reflect.Method;\n" +ln+
                "import java.lang.reflect.Proxy;\n" +ln+
                "import java.lang.reflect.UndeclaredThrowableException;"+ln);
        src.append("public final class $Proxy0  implements "+interfaces[0].getName()+" {"+ln);

        src.append("CMInvocationHandler h;"+ln);

        src.append("public $Proxy0(CMInvocationHandler h){"+ln);
        src.append("this.h=h;"+ln);
        src.append("}"+ln);

        for (Method method : interfaces[0].getMethods()) {
            src.append("public "+method.getReturnType().getName()+" "+method.getName()+" () throws Throwable{"+ln);
            src.append("Method m="+"Class.forName(\""+interfaces[0].getName()+"\").getMethod(\""+method.getName()+"\",new Class[]{});"+ln);
            src.append("this.h.invoke(this,m,null);"+ln);
            src.append("}"+ln);
        }
        src.append("}");
        return src.toString();
    }
}
