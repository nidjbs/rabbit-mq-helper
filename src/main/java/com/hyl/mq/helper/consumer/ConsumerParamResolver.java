package com.hyl.mq.helper.consumer;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author huayuanlin
 * @date 2021/10/24 15:29
 * @desc the interface desc
 */
public interface ConsumerParamResolver {

    /**
     * resole the execution parameters of the consumer method
     *
     * @param invocation invocation
     * @return params
     */
    InvocationConsumerParam resole(MethodInvocation invocation);


}
