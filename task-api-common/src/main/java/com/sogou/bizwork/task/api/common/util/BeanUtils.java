package com.sogou.bizwork.task.api.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

/**
 * @title BeanUtils
 * @description 1，兼容基本类型与包装类型（BeanCopier不支持）。2，兼容不规范setter（BeanCopier不支持，apache的BeanUtils不支持）
 * 3，支持copy类型检测（apache的BeanUtils不支持，spring的BeanUtils不支持）
 * @author tianzhen
 * @date 2015-3-4
 * @version 1.0
 */
public class BeanUtils {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(BeanUtils.class);
    private static final String SETTER_PREFIX = "set";

    public static void copy(Object source, Object target) {
        try {
            //基于内省机制获取bean的属性
            BeanInfo targetBeanInfo = Introspector.getBeanInfo(target.getClass());

            //根据getter来判断有哪些属性
            PropertyDescriptor[] targetProps = targetBeanInfo.getPropertyDescriptors();

            for (PropertyDescriptor targetProp : targetProps) {
                try {
                    //获取source与target相同name的属性
                    PropertyDescriptor sourceProp = getPropertyDescriptor(source.getClass(), targetProp.getName());

                    Method targetWriteMethod = targetProp.getWriteMethod();
                    if (targetWriteMethod == null) {
                        //如果不存在标准的setter，可能存在public xxx(非void) setXXX方法（譬如thrift生成的setter）
                        MethodDescriptor methodDesc = getMethodDescriptor(target.getClass(), SETTER_PREFIX
                                + targetProp.getName().substring(0, 1).toUpperCase()
                                + targetProp.getName().substring(1));
                        if (methodDesc != null) {
                            targetWriteMethod = methodDesc.getMethod();
                        }

                    }

                    //如果target具有setter方法，并且source具有getter方法
                    if (targetWriteMethod != null && sourceProp != null && sourceProp.getReadMethod() != null) {
                        Object value = getValue(sourceProp.getReadMethod(), source, new Object[0]);
                        if (value != null) {
                            //判断getter与setter的参数类型是否匹配，否则报IllegalArgumentException: argument type mismatch
                            if (value.getClass() == targetWriteMethod.getParameterTypes()[0]) {
                                setValue(targetWriteMethod, target, new Object[] { value });
                            } else if (value.getClass().isPrimitive()
                                    || targetWriteMethod.getParameterTypes()[0].isPrimitive()) {
                                setValue(targetWriteMethod, target, new Object[] { value });
                            } else if (targetWriteMethod.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
                                setValue(targetWriteMethod, target, new Object[] { value });
                            }
                        }

                    }
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Throwable ex) {
            logger.error("Could not copy properties from source to target", ex);
        }

    }

    private static Object getValue(Method method, Object obj, Object[] args) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        if (method == null || obj == null) {
            logger.info("Method method, Object obj must not be null");
            return null;
        }

        if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }

        return method.invoke(obj, args);
    }

    private static void setValue(Method method, Object obj, Object[] args) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        if (method == null || obj == null) {
            logger.info("Method method, Object obj must not be null");
        }

        if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }

        method.invoke(obj, args);
    }

    private static MethodDescriptor getMethodDescriptor(Class<?> clz, String methodName) throws IntrospectionException {
        if (clz == null || StringUtils.isBlank(methodName)) {
            logger.info("Class clz, String methodName must not be null");
            return null;
        }

        BeanInfo beanInfo = Introspector.getBeanInfo(clz);

        if (beanInfo != null) {
            MethodDescriptor[] methods = beanInfo.getMethodDescriptors();
            for (MethodDescriptor method : methods) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
        }

        return null;
    }

    private static PropertyDescriptor getPropertyDescriptor(Class<?> clz, String propertyName)
            throws IntrospectionException {
        if (clz == null) {
            logger.info("Class clz, String methodName must not be null");
            return null;
        }

        BeanInfo beanInfo = Introspector.getBeanInfo(clz);

        if (beanInfo != null) {
            PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor prop : props) {
                if (prop.getName().equals(propertyName)) {
                    return prop;
                }
            }
        }

        return null;
    }

}
