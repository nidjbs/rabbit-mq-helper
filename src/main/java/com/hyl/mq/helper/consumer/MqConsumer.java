package com.hyl.mq.helper.consumer;

/**
 * @author huayuanlin
 * @date 2021/11/18 10:29
 * @desc the interface desc
 */
public interface MqConsumer {

    /**
     * consumer logic
     *
     * @param consumerParam consumerParam
     * @return result
     */
    Object consumer(InvocationConsumerParam consumerParam);

}
