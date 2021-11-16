package com.hyl.mq.helper.util;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author huayuanlin
 * @date 2021/10/24 15:41
 * @desc the class desc
 */
public class MethodInvocationUtil {


    private MethodInvocationUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtain the Method object annotated in the invocation
     *
     * @param invocation invocation
     * @param clazz 注解class
     * @return method
     */
    @Nullable
    public static Method obtainAnnotationMethod(MethodInvocation invocation, Class<? extends Annotation> clazz) {
        // may be is a abstract method
        Method method = invocation.getMethod();
        if (method.getAnnotation(clazz) != null) {
            return method;
        }
        Object targetObj = invocation.getThis();
        Method targetMethod = ReflectionUtils.findMethod(targetObj.getClass(),
                method.getName(), method.getParameterTypes());
        if (targetMethod != null && targetMethod.getAnnotation(clazz) != null) {
            return targetMethod;
        }
        return null;
    }

}
