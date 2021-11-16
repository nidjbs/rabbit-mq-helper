package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.annotation.HpRabbitListener;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

/**
 * @author huayuanlin
 * @date 2021/10/22 17:12
 * @desc the class desc
 */
public class HpRabbitListenerMethodInterceptor extends AbstractMethodInterceptor {

    private final ConsumerExecutor<InvocationConsumerParam> executor = SmartConsumerExecutor.INSTANCE;

    private final ConsumerParamResolver consumerParamResolver = SimpleConsumerParamResolver.INSTANCE;

    @Override
    protected Class<? extends Annotation> annotation() {
        return HpRabbitListener.class;
    }

    @Override
    protected Object doIntercept(MethodInvocation invocation) throws Throwable {
        return executor.doExecute(consumerParamResolver.resole(invocation));
    }




}
