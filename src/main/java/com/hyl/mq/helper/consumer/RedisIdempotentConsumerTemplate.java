package com.hyl.mq.helper.consumer;

/**
 * @author huayuanlin
 * @date 2021/10/24 23:25
 * @desc the class desc
 */
public class RedisIdempotentConsumerTemplate extends IdempotentConsumerTemplate {

    public static final RedisIdempotentConsumerTemplate INSTANCE = new RedisIdempotentConsumerTemplate();

    protected RedisIdempotentConsumerTemplate() {
        super(RedisIdempotentLock.INSTANCE);
    }
}
