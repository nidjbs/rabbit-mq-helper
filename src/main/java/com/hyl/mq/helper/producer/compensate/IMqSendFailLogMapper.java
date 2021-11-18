package com.hyl.mq.helper.producer.compensate;

/**
 * @author huayuanlin
 * @date 2021/11/18 20:38
 * @desc the interface desc
 */
public interface IMqSendFailLogMapper {


    /**
     * add mqSendFailLog
     *
     * @param mqSendFailLog mqSendFailLog
     */
    void addFailLog(MqSendFailLogDO mqSendFailLog);


}
