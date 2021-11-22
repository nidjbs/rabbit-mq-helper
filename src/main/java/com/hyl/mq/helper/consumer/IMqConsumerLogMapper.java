package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.common.CompensateState;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author huayuanlin
 * @date 2021/10/24 22:34
 * @desc the interface desc
 */
public interface IMqConsumerLogMapper {

    /**
     * addMessageRetries
     *
     * @param mqLogUid           mqLogUid
     * @param msg                msg
     * @param consumerQueueNames consumerQueueNames
     * @return retry times
     */
    @Transactional(rollbackFor = Exception.class)
    int addRetryTimes(String mqLogUid, String msg, Set<String> consumerQueueNames);


    /**
     * 尝试新增mqLog
     *
     * @param mqLogUid mqLogUid
     * @param msg      msg
     * @return result
     */
    boolean tryAddMqLog(String mqLogUid, String msg);

    /**
     * Add message record
     *
     * @param mqLogUid           mqLogUid
     * @param msg                msg
     * @param consumerQueueNames consumerQueueNames
     * @param compensateState    compensateState
     */
    void addMqLog(String mqLogUid, String msg, Set<String> consumerQueueNames, CompensateState compensateState);

    /**
     * update consumer mq msg state
     *
     * @param mqLogUid        mqLogUid
     * @param compensateState compensateState
     */
    void updateState(String mqLogUid, CompensateState compensateState);

    /**
     * try update consumer mq msg state
     *
     * @param id              id
     * @param source          source state
     * @param compensateState compensateState
     */
    boolean tryUpdateState(Long id, CompensateState source, CompensateState compensateState);

    /**
     * query list MqConsumerLog
     *
     * @param batchSize batchSize
     * @param appName   appName
     * @return list
     */
    List<MqConsumerLogDO> listMqConsumerLog(int batchSize, String appName);


}
