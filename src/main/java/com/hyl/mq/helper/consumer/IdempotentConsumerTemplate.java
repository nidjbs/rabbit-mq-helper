package com.hyl.mq.helper.consumer;

/**
 * @author huayuanlin
 * @date 2021/10/24 14:30
 * @desc the class desc
 */
public abstract class IdempotentConsumerTemplate extends ConsumerTemplate {


    private final IdempotentLock idempotentLock;

    protected IdempotentConsumerTemplate(IdempotentLock idempotentLock) {
        this.idempotentLock = idempotentLock;
    }

    @Override
    protected boolean preCheck(ConsumerParamHolder consumerParam) {
        String msgUid = consumerParam.getMsgUid();
        Boolean obtainLock = idempotentLock.obtainLock(msgUid, consumerParam.getMsgPayloadStr());
        return Boolean.TRUE.equals(obtainLock);
    }

    @Override
    protected void onPreCheckFail(ConsumerParamHolder consumerParam) {
        // 获取锁失败，即当前是重复消费，直接将消息确认
        basicAck(consumerParam);
    }

    @Override
    protected void onConsumerFail(ConsumerParamHolder consumerParam) {
        // 消费失败，添加失败记录后直接确认，等待补偿
        mqLogMapper.getBean().addMqLog(consumerParam.getMsgUid(), consumerParam.getMsgPayloadStr());
        basicAck(consumerParam);
    }
}
