package com.hyl.mq.helper.consumer;

/**
 * @author huayuanlin
 * @date 2021/10/24 14:35
 * @desc the interface desc
 */
public interface Executor<T> {


    Object doExecute(T t);

}
