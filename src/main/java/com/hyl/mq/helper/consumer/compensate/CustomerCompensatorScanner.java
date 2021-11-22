package com.hyl.mq.helper.consumer.compensate;

import com.hyl.mq.helper.consumer.MqConsumerHolderContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author huayuanlin
 * @date 2021/11/22 14:01
 * @desc the class desc
 */
@SuppressWarnings("all")
public class CustomerCompensatorScanner implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, CustomerCompensator> beansOfType = event.getApplicationContext().getBeansOfType(CustomerCompensator.class);
        if (CollectionUtils.isEmpty(beansOfType)) {
            return;
        }
        beansOfType.values().forEach(customerCompensator -> {
            Set<String> queueNames = customerCompensator.queueNames();
            if (CollectionUtils.isEmpty(queueNames)) {
                return;
            }
            MqConsumerHolderContext.registerHolder(queueNames, getCompensateMethod(customerCompensator), customerCompensator);
        });
    }

    private Method getCompensateMethod(CustomerCompensator customerCompensator) {
        Class<? extends CustomerCompensator> aClass = customerCompensator.getClass();
        Method[] declaredMethods = ReflectionUtils.getUniqueDeclaredMethods(aClass);
        for (Method method : declaredMethods) {
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (CustomerCompensator.DO_COMPENSATE_METHOD_NAME.equals(name)
                    && parameterTypes[0] != Object.class) {
                return method;
            }
        }
        return null;
    }

}
