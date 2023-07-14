package com.webank.scaffold.util;

import java.lang.reflect.Method;

public class ReflectUtil {

    public static Method getMethod(Class clazz, String methodName, final Class[] paramClasses) {
        if (clazz == null){
            return null;
        }
        try{
            Method method = clazz.getDeclaredMethod(methodName, paramClasses);
            return method;
        }
        catch (NoSuchMethodException ex){
            if (clazz.getSuperclass() == null){
                return null;
            }
            return getMethod(clazz.getSuperclass(), methodName, paramClasses);
        }
    }

    public static Object invokeSuper(final Object obj, final String methodName,
                                     final Class[] classes, final Object[] objects) {
        try {
            Method method = getMethod(obj.getClass().getSuperclass(), methodName, classes);
            if (method == null){
                throw new NoSuchMethodException(methodName);
            }
            method.setAccessible(true);// 调用private方法的关键一句话
            return method.invoke(obj, objects);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
