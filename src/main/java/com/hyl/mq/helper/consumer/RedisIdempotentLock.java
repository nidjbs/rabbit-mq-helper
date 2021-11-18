package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.config.MqHelperConfig;
import com.hyl.mq.helper.util.RedisUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author huayuanlin
 * @date 2021/10/24 23:21
 * @desc the class desc
 */
public class RedisIdempotentLock implements IdempotentLock {

    public static final RedisIdempotentLock INSTANCE = new RedisIdempotentLock();

    protected final SingleSpringBeanWrapper<MqHelperConfig> mqConfig = new SingleSpringBeanWrapper<MqHelperConfig>() {
    };

    private static final String REDIS_KEY_PRE = "mq_hp:consumer_idempotent_lock";

    @Override
    public Boolean obtainLock(String key, String msg) {
        Long redisIdempotentLockTtl = mqConfig.getBean().getConsumer().getRedisIdempotentLockTtl();
        return RedisUtil.valueOps().setIfAbsent(REDIS_KEY_PRE + key, "1", redisIdempotentLockTtl, TimeUnit.MILLISECONDS);
    }
}
