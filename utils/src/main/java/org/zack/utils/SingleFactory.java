package org.zack.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SingleFactory <T extends SingleFactory>  {

    protected SingleFactory(){}

    private static SingleFactory instance = new SingleFactory();

    private Map<Class<T>,T> factoryMap = new HashMap<>();



    /**
     * 初始化方法
     * @return 工厂实例
     */
    protected static SingleFactory init(){
        return new SingleFactory();
    }

    public T getFactory(String clazzpath) throws ClassNotFoundException {
        Class<T> clazz = (Class<T>)Class.forName(clazzpath);
        return getFactory(clazz);
    }

    public T getFactory(Class<T> clazz){
        T sunInstance = factoryMap.get(clazz);
        if (sunInstance == null){
            synchronized (instance){
                if (sunInstance == null){
                    sunInstance = addFactory(clazz);
                }
            }
        }
        return sunInstance;
    }

    protected T addFactory(Class<T> clazz) {
        T sunInstance = null;
        try {
            Method init = clazz.getDeclaredMethod("init");
            init.setAccessible(true);
            sunInstance = (T) init.invoke(null);
            addFactory(clazz,sunInstance);
            return sunInstance;
        } catch (Exception e) {
            throw new RuntimeException("创建"+clazz.getName()+"实例失败",e);
        }
    }

    protected T addFactory(Class<T> clazz,T sunInstance){
        return factoryMap.put(clazz,sunInstance);
    }

    protected T addFactory(){
        getClass().getGenericSuperclass();
        Class clazz = getClass();
        return (T) addFactory(clazz);
    }
    public static SingleFactory getInstance(){
        return instance;
    }
}
