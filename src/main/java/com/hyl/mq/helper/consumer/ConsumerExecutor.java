package com.hyl.mq.helper.consumer;

/**
 * @author huayuanlin
 * @date 2021/10/24 14:35
 * @desc the interface desc
 */
public interface ConsumerExecutor<T> {


    /**
     * ConsumerExecutor
     *
     * @param t params
     * @return final result
     */
    Object doExecute(T t);

}
