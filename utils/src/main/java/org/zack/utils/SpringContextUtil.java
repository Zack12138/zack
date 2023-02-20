/**  
 * @Project Name:util  
 * @File Name:SpringContextUtil.java  
 * @Package Name:org.zack.utils  
 * @Date:2021年3月20日下午2:12:47  
 * Copyright (c) 2021, Zack All Rights Reserved.  
 *  
*/  
  
package org.zack.utils;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;

@SuppressWarnings({"rawtypes","unchecked"})
@Repository
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext; //Spring应用上下文环境

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     * @param applicationContext
     * @throws
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws
     */
    public static Object getBean(String name)  {
        return applicationContext.getBean(name);
    }

    /**
     * 获取类型为requiredType的对象
     * 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
     * @param name bean注册名
     * @param requiredType 返回对象类型
     * @return Object 返回requiredType类型对象
     * @throws
     */
	public static Object getBean(String name, Class requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
     * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     * @param name
     * @return boolean
     * @throws NoSuchBeanDefinitionException
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }

    /**
     * @param name
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException
     */
    
	public static Class getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getAliases(name);
    }
    
    /***
     * 根据一个bean的类型获取配置文件中相应的bean
     */
    public static <T> T getBeanByClass(Class<T> requiredType) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(requiredType);
    }

    /**
     * <b>功能：</b>根据@service注解的名称获取service的bean<br>
     * @param <T>
     * @param clazz
     * @return
     * @throws RuntimeException
     */
	public static <T> T getServiceByClass(Class<T> clazz) {
        String[] beanNames = applicationContext.getBeanNamesForType(clazz);
        if (beanNames.length == 0) {
            throw new RuntimeException(clazz.getName() + "类没有注解bean名");
        }
        return (T) SpringContextUtil.getBean(beanNames[0], clazz);
    }

    /**
     * <b>功能：</b>根据@service注解的名称获取service的bean<br>
     * @param <T>
     * @param clazz
     * @return
     * @throws RuntimeException
     */
    public static <T> T[] getServiceByClasses(Class<T> clazz) {
        String[] beanNames = applicationContext.getBeanNamesForType(clazz);
        if (beanNames.length == 0) {
            throw new RuntimeException(clazz.getName() + "类没有注解bean名");
        }
        T[] resule = (T[]) Array.newInstance(clazz,beanNames.length);
        for (int i = 0;i<beanNames.length;i++) {
            resule[i] = applicationContext.getBean(beanNames[i], clazz);
        }
        return resule;
    }
}
 