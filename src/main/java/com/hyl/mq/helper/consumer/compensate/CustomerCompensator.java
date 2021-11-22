package com.hyl.mq.helper.consumer.compensate;

import java.util.Set;

/**
 * @author huayuanlin
 * @date 2021/11/22 11:02
 * @desc the interface desc
 */
public interface CustomerCompensator<P> {

    String DO_COMPENSATE_METHOD_NAME = "doCompensate";

    /**
     * Compensator queue names
     *
     * @return queue names
     */
    Set<String> queueNames();

    /**
     * doCompensate
     *
     * @param p msg param, If it is a string type, the original message will be passed in,
     *          if it is an object, the message will first be converted to the object you need
     */
    void doCompensate(P p);

}
