package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.util.RedisUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author huayuanlin
 * @date 2021/10/24 23:21
 * @desc the class desc
 */
public class RedisIdempotentLock implements IdempotentLock {

    public static final RedisIdempotentLock INSTANCE = new RedisIdempotentLock();

    @Override
    public Boolean obtainLock(String key, String msg) {
        return RedisUtil.valueOps().setIfAbsent(key, "1", 60, TimeUnit.MINUTES);
    }
}
