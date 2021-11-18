package com.hyl.mq.helper.consumer.compensate;

import java.util.List;

/**
 * @author huayuanlin
 * @date 2021/11/16 23:13
 * @desc the interface desc
 */
public interface CompensateHandler {

    List<String> getMsgItems();

    void doCompensate(List<String> msgList, String queueName);


}
