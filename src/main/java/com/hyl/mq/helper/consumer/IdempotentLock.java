package com.hyl.mq.helper.consumer;

/**
 * @author huayuanlin
 * @date 2021/10/24 22:12
 * @desc the interface desc
 */
public interface IdempotentLock {

    /**
     * getLock
     * @param key key
     * @param msg msg
     * @return result
     */
    Boolean obtainLock(String key,String msg);

}
