package com.hyl.mq.helper.consumer;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author huayuanlin
 * @date 2021/08/07 20:43
 * @desc the class desc
 */
public abstract class AbstractAopProxyCreator extends AbstractAutoProxyCreator {


    private final Set<String> ALREADY_PROXY_BEAN_SET = new HashSet<>();

    private final Object LOCK = new Object();

    private final MethodInterceptor interceptor;

    public AbstractAopProxyCreator(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }


    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        synchronized (LOCK) {
            if (ALREADY_PROXY_BEAN_SET.contains(beanName)) {
                return bean;
            }
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            if (!existAnnotation(targetClass)) {
                return bean;
            }
            try {
                // use cur interceptor to new proxy
                if (!AopUtils.isAopProxy(bean)) {
                    bean = super.wrapIfNecessary(bean, beanName, cacheKey);
                } else {
                    // add cur interceptor to advisors
                    AdvisedSupport curProxyAdvisedSupportField = getCurProxyAdvisedSupportField(bean);
                    Advisor[] advisors = buildAdvisors(beanName, new Object[]{interceptor});
                    Stream.of(advisors).forEach(curProxyAdvisedSupportField::addAdvisor);
                }
                ALREADY_PROXY_BEAN_SET.add(beanName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return bean;
        }
    }

    /**
     * aop annotation
     *
     * @return annotation clazz
     */
    protected abstract Class<? extends Annotation> aopOpLogAnnotation();


    /**
     * is exist annotation
     *
     * @param targetClass target obj
     * @return boolean
     */
    private boolean existAnnotation(Class<?> targetClass) {
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            Annotation annotation = method.getAnnotation(aopOpLogAnnotation());
            if (annotation != null) {
                return true;
            }
        }
        return false;
    }


    /**
     * get aop bean Advisor manager(support)
     *
     * @param proxy proxy
     * @return bean Advisor
     * @throws Exception ex
     */
    private AdvisedSupport getCurProxyAdvisedSupportField(Object proxy) throws Exception {
        Field h;
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            h = proxy.getClass().getSuperclass().getDeclaredField("h");
        } else {
            h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        }
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        return (AdvisedSupport) advised.get(dynamicAdvisedInterceptor);
    }


    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> aClass, String s, TargetSource targetSource) throws BeansException {
        return new Object[]{interceptor};
    }
}
