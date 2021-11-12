package com.hyl.mq.helper.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author huayuanlin
 * @date 2021-05-26 20:56:02
 * @desc the SpringBeanUtil
 */
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if (applicationContext == null) {
            applicationContext = context;
        }
    }

    public static <T> T getBeanByType(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    public static <T> T safeGetBeanByType(Class<T> tClass) {
        Map<String, T> beansOfType = applicationContext.getBeansOfType(tClass);
        if (CollectionUtils.isEmpty(beansOfType)) {
            return null;
        }
        Collection<T> values = beansOfType.values();
        return values.stream().findFirst().orElse(null);
    }


    @SuppressWarnings("unchecked")
    public static <T> T getBeanByName(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
