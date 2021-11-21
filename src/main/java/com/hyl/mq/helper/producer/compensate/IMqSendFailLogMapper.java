package com.hyl.mq.helper.producer.compensate;

import com.hyl.mq.helper.common.CompensateState;

import java.util.List;

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


    /**
     * list compensate log
     *
     * @param batchSize batchSize
     * @return log list
     */
    List<MqSendFailLogDO> listCompensateLog(int batchSize);

    /**
     * updateState
     *
     * @param id              id
     * @param compensateState compensateState
     * @param original        original CompensateState
     * @return result
     */
    boolean updateState(Long id, CompensateState original, CompensateState compensateState);


}
