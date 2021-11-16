package com.hyl.mq.helper.consumer;

import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author huayuanlin
 * @date 2021/10/24 22:34
 * @desc the interface desc
 */
public interface IMqLogMapper {

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
     * @param msg msg
     * @return result
     */
    boolean tryAddMqLog(String mqLogUid, String msg);

    /**
     * Add message record
     *
     * @param mqLogUid mqLogUid
     * @param msg      msg
     * @param consumerQueueNames   consumerQueueNames
     */
    @Transactional(rollbackFor = Exception.class)
    void addMqLog(String mqLogUid, String msg, Set<String> consumerQueueNames);


}
