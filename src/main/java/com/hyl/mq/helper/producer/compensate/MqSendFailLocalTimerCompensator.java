package com.hyl.mq.helper.producer.compensate;

import com.hyl.mq.helper.common.Compensator;
import com.hyl.mq.helper.util.SafeExecuteUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author huayuanlin
 * @date 2021/11/19 16:03
 * @desc the class desc
 */
public class MqSendFailLocalTimerCompensator implements Compensator<MqSendFailLogDO>, ApplicationListener<ContextRefreshedEvent> {

    private final Compensator<MqSendFailLogDO> compensatorDelegate = DefaultMqSenderFailCompensator.INSTANCE;

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
            r -> new Thread(r, "mq send fail compensator"));


    @Override
    public void doCompensate() {
        compensatorDelegate.doCompensate();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        scheduledExecutorService.scheduleAtFixedRate(
                () -> SafeExecuteUtil.safeExecute(this::doCompensate, Exception.class),
                5000, 5000, TimeUnit.MILLISECONDS);
    }
}
