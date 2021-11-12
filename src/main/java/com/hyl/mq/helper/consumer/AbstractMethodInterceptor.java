package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.util.MethodInvocationUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author huayuanlin
 * @date 2021/09/12 23:32
 * @desc the class desc
 */
public abstract class AbstractMethodInterceptor implements MethodInterceptor {

    @Override
    public final Object invoke(MethodInvocation invocation) throws Throwable {
        if (!containAnnotation(invocation)) {
            return invocation.proceed();
        } else {
            return doIntercept(invocation);
        }
    }

    private boolean containAnnotation(MethodInvocation invocation) {
        return obtainCurMethod(invocation).isPresent();
    }

    protected Optional<Method> obtainCurMethod(MethodInvocation invocation) {
        Method method = MethodInvocationUtil.obtainAnnotationMethod(invocation, annotation());
        return Optional.ofNullable(method);
    }


    /**
     * annotation class on interception method
     *
     * @return annotation
     */
    protected abstract Class<? extends Annotation> annotation();

    /**
     * do intercept
     *
     * @return result
     * @param invocation invocation
     * @throws Throwable exx
     */
    protected abstract Object doIntercept(MethodInvocation invocation) throws Throwable;
}
