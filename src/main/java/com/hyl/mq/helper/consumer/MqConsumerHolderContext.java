package com.hyl.mq.helper.consumer;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author huayuanlin
 * @date 2021/10/20 12:47
 * @desc the class desc
 */
public class MqConsumerHolderContext {

    public static final MqConsumerHolderContext INSTANCE = new MqConsumerHolderContext();

    /*** k：queueName，v: consumer holder */
    private static final Map<String, ConsumerHolder> CONSUMER_HOLDER_MAP = new HashMap<>(16);


    public void registerHolder(Set<String> queueNames, Method method, Object targetBean) {
        Assert.notNull(method, "register method is null!");
        Assert.notNull(targetBean, "targetBean is null!");
        if (CollectionUtils.isEmpty(queueNames)) {
            return;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        Assert.isTrue(parameterTypes.length != 0, "your consumer method params is empty! method is:" + method);
        ConsumerHolder holder = new ConsumerHolder();
        holder.setBean(targetBean);
        holder.setMethod(method);
        holder.setMsgClazz(parameterTypes[0]);
        queueNames.forEach(queueName -> CONSUMER_HOLDER_MAP.put(queueName, holder));
    }


    public void invokeHolderMethod(String queueName) {
        Assert.hasText(queueName, "queueName");
        ConsumerHolder holder = CONSUMER_HOLDER_MAP.get(queueName);
        ReflectionUtils.invokeMethod(holder.getMethod(), holder.getBean());
    }


    protected static class ConsumerHolder {

        private Object bean;

        private Method method;

        private Class<?> msgClazz;

        public Class<?> getMsgClazz() {
            return msgClazz;
        }

        public void setMsgClazz(Class<?> msgClazz) {
            this.msgClazz = msgClazz;
        }

        public Object getBean() {
            return bean;
        }

        public void setBean(Object bean) {
            this.bean = bean;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }
    }


}
