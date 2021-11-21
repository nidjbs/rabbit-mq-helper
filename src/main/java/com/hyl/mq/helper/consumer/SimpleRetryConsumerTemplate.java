package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.common.CompensateState;
import com.hyl.mq.helper.util.SafeExecuteUtil;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author huayuanlin
 * @date 2021/10/24 16:53
 * @desc the class desc
 */
public class SimpleRetryConsumerTemplate extends ConsumerTemplate {


    public static final SimpleRetryConsumerTemplate INSTANCE = new SimpleRetryConsumerTemplate();

    private SimpleRetryConsumerTemplate() {

    }

    /*** for delayed mq nack*/
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR = new ScheduledThreadPoolExecutor(3,
            r -> new Thread(r, "mq nack monitor"));

    @Override
    protected void onConsumerFail(ConsumerParamHolder consumerParam) {
        // add retry times
        int retryTimes = mqLogMapper.getBean().addRetryTimes(consumerParam.getMsgUid(),
                consumerParam.getMsgPayloadStr(), consumerParam.getConsumerQueueNames());
        if (retryTimes >= consumerParam.getRetryTimes()) {
            LOGGER.warn("this message consumer is maximum number of times,uid is:" + consumerParam.getMsgUid());
            // set for state compensation
            mqLogMapper.getBean().updateState(consumerParam.getMsgUid(), CompensateState.WAIT_COMPENSATE);
            basicAck(consumerParam);
        } else {
            long retrySleepTimeOut = consumerParam.getRetrySleepTimeOut();
            if (retrySleepTimeOut > 0) {
                // delayed nack,avoid blocking the consumer thread
                SCHEDULED_EXECUTOR.schedule(
                        () -> SafeExecuteUtil.safeExecute(() -> basicNack(consumerParam), Exception.class),
                        retrySleepTimeOut, TimeUnit.MILLISECONDS);
            } else {
                // nack
                basicNack(consumerParam);
            }
        }
    }


}
