package com.hyl.mq.helper.consumer;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author huayuanlin
 * @date 2021/10/24 15:35
 * @desc the class desc
 */
public abstract class InvocationConsumerParam {

    private MethodInvocation source;

    private InvokeCallBack<MethodInvocation, Object> consumerLogic;

    public MethodInvocation getSource() {
        return source;
    }

    public void setSource(MethodInvocation source) {
        this.source = source;
    }

    public Object invokeConsumerLogic() {
        return consumerLogic.callBack(source);
    }

    public void setConsumerLogic(InvokeCallBack<MethodInvocation, Object> consumerLogic) {
        this.consumerLogic = consumerLogic;
    }

    @FunctionalInterface
    interface InvokeCallBack<P, T> {

        T callBack(P p);

    }
}
