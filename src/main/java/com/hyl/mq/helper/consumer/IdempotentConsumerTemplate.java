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
        // Failed to acquire the lock, that is, the current is repeated consumption, directly confirm the message
        basicAck(consumerParam);
    }

    @Override
    protected void onConsumerFail(ConsumerParamHolder consumerParam) {
        // Consumption fails, confirm directly after adding the failed record, waiting for compensation
        mqLogMapper.getBean().addMqLog(consumerParam.getMsgUid(),
                consumerParam.getMsgPayloadStr(), consumerParam.getConsumerQueueNames());
        basicAck(consumerParam);
    }
}
