package com.hyl.mq.helper.consumer;

/**
 * @author huayuanlin
 * @date 2021/10/24 22:58
 * @desc the class desc
 */
public class DbIdempotentLock implements IdempotentLock {

    public static final IdempotentLock INSTANCE = new DbIdempotentLock();

    private final SingleSpringBeanWrapper<IMqLogMapper> mqLogMapper = new SingleSpringBeanWrapper<IMqLogMapper>() {
    };


    @Override
    public Boolean obtainLock(String key, String msg) {
        return mqLogMapper.getBean().tryAddMqLog(key, msg);
    }
}
