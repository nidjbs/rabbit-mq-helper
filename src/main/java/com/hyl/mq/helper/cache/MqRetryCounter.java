package com.hyl.mq.helper.cache;


import com.hyl.mq.helper.producer.ConfirmedMsgWrapper;

/**
 * @author huayuanlin
 * @date 2021/09/22 14:43
 * @desc the class desc
 */
public class MqRetryCounter extends StandardRedisCache<ConfirmedMsgWrapper> {

    public static final MqRetryCounter DEFAULT = new MqRetryCounter(15);

    protected MqRetryCounter(int keyTimeOut) {
        super(keyTimeOut);
    }


    @Override
    protected Class<ConfirmedMsgWrapper> curGenericClass() {
        return ConfirmedMsgWrapper.class;
    }
}
