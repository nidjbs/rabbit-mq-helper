package com.hyl.mq.helper.consumer;

/**
 * @author huayuanlin
 * @date 2021/10/24 22:32
 * @desc the class desc
 */
public class DbIdempotentConsumerTemplate extends IdempotentConsumerTemplate {

    public static final DbIdempotentConsumerTemplate INSTANCE = new DbIdempotentConsumerTemplate();

    protected DbIdempotentConsumerTemplate() {
        super(DbIdempotentLock.INSTANCE);
    }

}
