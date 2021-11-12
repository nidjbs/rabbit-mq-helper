package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.annotation.HpRabbitListener;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.annotation.Annotation;

/**
 * @author huayuanlin
 * @date 2021/10/22 16:58
 * @desc the class desc
 */
public class HpRabbitListenerAopProxyCreator extends AbstractAopProxyCreator {


    public HpRabbitListenerAopProxyCreator(MethodInterceptor interceptor) {
        super(interceptor);
    }

    @Override
    protected Class<? extends Annotation> aopOpLogAnnotation() {
        return HpRabbitListener.class;
    }


}
