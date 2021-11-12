package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.annotation.HpRabbitListener;
import com.hyl.mq.helper.util.AnnotationUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * @author huayuanlin
 * @date 2021/09/15 16:31
 * @desc 本后置处理器用于给@HpRabbitListener 注解，将HpRabbitListener注解的属性复制到@RabbitListener中，
 * 这样在RabbitListener中的扫描器中就能扫到配置的属性，让@HpRabbitListener一定意义上等于@RabbitListener
 */
public class HpRabbitConsumerBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor, Ordered {


    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        ReflectionUtils.doWithLocalMethods(targetClass, method -> {
            HpRabbitListener hpRabbitListener = AnnotationUtils.findAnnotation(method, HpRabbitListener.class);
            RabbitListener rabbitListener = AnnotationUtils.findAnnotation(method, RabbitListener.class);
            if (hpRabbitListener == null || rabbitListener == null) {
                return;
            }
            Set<String> queueNames = AnnotationUtil.resolveQueueNames(hpRabbitListener);
            // 将消费者方法放入上下文，方便补偿逻辑
            MqConsumerHolderContext.INSTANCE.registerHolder(queueNames, method, bean);
            // 将HpRabbitListener 属性复制给RabbitListener
            Map<String, Object> memberValuesMap = getAnnotationMembers(rabbitListener.getClass(), rabbitListener);
            Map<String, Object> hpMemberValuesMap = getAnnotationMembers(hpRabbitListener.getClass(), hpRabbitListener);
            memberValuesMap.putAll(hpMemberValuesMap);
        });
        return bean;
    }


    @SuppressWarnings("unchecked")
    private Map<String, Object> getAnnotationMembers(Class<?> annotation, Object enumObj)
            throws IllegalAccessException {
        // 下述反射不用担心报错，注解实现原理就是Jdk动态代理
        Field proxyHandler = ReflectionUtils.findField(annotation, "h");
        assert proxyHandler != null;
        ReflectionUtils.makeAccessible(proxyHandler);
        Object proxyHandlerValue = proxyHandler.get(enumObj);
        // 此处注解一般是spring帮我们生成的代理类（用于处理别名情况） @see SynthesizedAnnotationInvocationHandler
        Field valueCache = ReflectionUtils.findField(proxyHandlerValue.getClass(), "valueCache");
        // 如果没有找到，从注解默认的代理中获得 @see sun.reflect.annotation.AnnotationInvocationHandler
        if (valueCache == null) {
            valueCache = ReflectionUtils.findField(proxyHandlerValue.getClass(), "memberValues");
        }
        // 不会为空...
        assert valueCache != null;
        ReflectionUtils.makeAccessible(valueCache);
        return (Map<String, Object>) valueCache.get(proxyHandlerValue);
    }

    @Override
    public int getOrder() {
        // 优先级大于org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor bean
        return HIGHEST_PRECEDENCE;
    }
}
