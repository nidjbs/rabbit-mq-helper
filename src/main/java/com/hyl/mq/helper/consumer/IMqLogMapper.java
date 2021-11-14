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
     * 添加消息重试次数
     *
     * @param mqLogUid           mqLogUid
     * @param msg                msg
     * @param consumerQueueNames consumerQueueNames
     * @return 重试次数
     */
    @Transactional(rollbackFor = Exception.class)
    int addRetryTimes(String mqLogUid, String msg, Set<String> consumerQueueNames);


    /**
     * 尝试新增mqLog
     *
     * @param mqLogUid mqLogUid
     * @return 是否成功
     */
    boolean tryAddMqLog(String mqLogUid, String msg);

    /**
     * 添加消息记录
     *
     * @param mqLogUid mqLogUid
     * @param msg      msg
     */
    @Transactional(rollbackFor = Exception.class)
    void addMqLog(String mqLogUid, String msg, Set<String> consumerQueueNames);


}
