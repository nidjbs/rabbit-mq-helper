package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.util.JsonUtil;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author huayuanlin
 * @date 2021/10/20 12:47
 * @desc the class desc
 */
public class MqConsumerHolderContext {

    private MqConsumerHolderContext() {
        throw new UnsupportedOperationException();
    }

    /*** k：queueName，v: consumer holder */
    private static final Map<String, ConsumerHolder> CONSUMER_HOLDER_MAP = new HashMap<>(16);


    public static void registerHolder(Set<String> queueNames, Method method, Object targetBean) {
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


    public static void invokeSingleConsumer(List<String> queueNames, String msg) {
        if (CollectionUtils.isEmpty(queueNames) || StringUtils.isEmpty(msg)) {
            return;
        }
        ConsumerHolder consumerHolder = null;
        for (String queueName : queueNames) {
            ConsumerHolder holder = CONSUMER_HOLDER_MAP.get(queueName);
            if (holder != null) {
                consumerHolder = holder;
                break;
            }
        }
        if (consumerHolder != null) {
            Method method = consumerHolder.getMethod();
            ReflectionUtils.makeAccessible(method);
            ReflectionUtils.invokeMethod(method, consumerHolder.getBean(), buildMethodParam(consumerHolder, msg));
        }
    }

    private static Object[] buildMethodParam(ConsumerHolder consumerHolder, String msgStr) {
        Class<?> msgClazz = consumerHolder.getMsgClazz();
        Method method = consumerHolder.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];
        if (msgClazz == String.class) {
            params[0] = msgStr;
        } else {
            params[0] = JsonUtil.fromJson(msgStr, msgClazz);
        }
        return params;
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
