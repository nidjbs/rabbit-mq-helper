package com.hyl.mq.helper.common;

/**
 * @author huayuanlin
 * @date 2021/11/19 14:55
 * @desc the interface desc
 */
public interface Compensator<T> {

    /**
     * 执行补偿
     */
    void doCompensate();


}
