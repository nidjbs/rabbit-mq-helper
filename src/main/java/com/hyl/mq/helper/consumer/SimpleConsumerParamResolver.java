package com.hyl.mq.helper.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.hyl.mq.helper.annotation.HpRabbitListener;
import com.hyl.mq.helper.common.ObjBuilder;
import com.hyl.mq.helper.exx.ConsumerException;
import com.hyl.mq.helper.exx.FrameworkException;
import com.hyl.mq.helper.exx.NeverHappenException;
import com.hyl.mq.helper.protocol.BaseMqMsg;
import com.hyl.mq.helper.util.AnnotationUtil;
import com.hyl.mq.helper.util.MethodInvocationUtil;
import com.rabbitmq.client.Channel;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author huayuanlin
 * @date 2021/10/24 15:30
 * @desc the class desc
 */
public class SimpleConsumerParamResolver implements ConsumerParamResolver {

    public static final ConsumerParamResolver INSTANCE = new SimpleConsumerParamResolver();

    private static final String MSG_ID_JSON_PATH = "$.uniqueId";

    @Override
    public InvocationConsumerParam resole(MethodInvocation invocation) {
        Assert.notNull(invocation, "invocation is null!");
        Method annotationMethod = MethodInvocationUtil.obtainAnnotationMethod(invocation, HpRabbitListener.class);
        if (annotationMethod == null) {
            throw new NeverHappenException("cur method not contain @HpRabbitListener annotation!");
        }
        HpRabbitListener hpRabbitListener = annotationMethod.getAnnotation(HpRabbitListener.class);
        if (hpRabbitListener == null) {
            throw new NeverHappenException("cur method not contain @HpRabbitListener annotation!");
        }
        boolean openIdempotent = hpRabbitListener.openIdempotent();
        Object[] args = invocation.getArguments();
        Object msg = args[0];
        String uid;
        // 取uid
        if (msg instanceof String) {
            uid = (String) JSONPath.eval(JSON.parseObject((String) msg), MSG_ID_JSON_PATH);
        } else if (msg instanceof BaseMqMsg) {
            uid = ((BaseMqMsg) msg).getUniqueId();
        } else {
            throw new FrameworkException("your msg not contain msg uid,please check sender or use" +
                    " original @RabbitListener annotation!");
        }
        Set<String> queueNames = AnnotationUtil.resolveQueueNames(hpRabbitListener);
        return ObjBuilder.create(ConsumerParamHolder::new)
                .of(ConsumerParamHolder::setSource, invocation)
                .of(ConsumerParamHolder::setConsumerLogic, this::invokeProceed)
                .of(ConsumerParamHolder::setMsgPayload, msg)
                .of(ConsumerParamHolder::setConsumerQueueNames, queueNames)
                .of(ConsumerParamHolder::setMsgUid, uid)
                .of(ConsumerParamHolder::setChannel, (Channel) args[1])
                .of(ConsumerParamHolder::setMessage, (Message) args[2])
                .of(ConsumerParamHolder::setRetry, hpRabbitListener.retryTimes() > 0)
                .of(ConsumerParamHolder::setRetryTimes, hpRabbitListener.retryTimes())
                .of(ConsumerParamHolder::setRetrySleepTimeOut, hpRabbitListener.retrySleepTimeOut())
                .of(ConsumerParamHolder::setOpenIdempotent, openIdempotent)
                .of(ConsumerParamHolder::setIdempotentType, hpRabbitListener.idempotentType())
                .of(ConsumerParamHolder::setWrapTx, hpRabbitListener.wrapTx())
                .of(ConsumerParamHolder::setTxTimeOut, hpRabbitListener.txTimeOut())
                .build();
    }

    protected Object invokeProceed(MethodInvocation invocation) {
        try {
            return invocation.proceed();
        } catch (Throwable throwable) {
            throw new ConsumerException(throwable);
        }
    }
}
