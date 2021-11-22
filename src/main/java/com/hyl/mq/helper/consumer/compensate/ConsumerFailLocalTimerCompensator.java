package com.hyl.mq.helper.consumer.compensate;

import com.hyl.mq.helper.common.Compensator;
import com.hyl.mq.helper.config.MqHelperConfig;
import com.hyl.mq.helper.consumer.MqConsumerLogDO;
import com.hyl.mq.helper.util.SafeExecuteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author huayuanlin
 * @date 2021/11/22 15:09
 * @desc the class desc
 */
public class ConsumerFailLocalTimerCompensator implements Compensator<MqConsumerLogDO>, ApplicationListener<ContextRefreshedEvent> {

    private final Compensator<MqConsumerLogDO> compensatorDelegate = DefaultLocalConsumerCompensator.INSTANCE;

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
            r -> new Thread(r, "mq consumer fail compensator"));

    @Autowired
    private MqHelperConfig mqHelperConfig;

    @Override
    public void doCompensate() {
        compensatorDelegate.doCompensate();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Long localConsumerPeriod = mqHelperConfig.getCompensator().getLocalConsumerPeriod();
        scheduledExecutorService.scheduleAtFixedRate(
                () -> SafeExecuteUtil.safeExecute(this::doCompensate, Exception.class),
                localConsumerPeriod, localConsumerPeriod, TimeUnit.MILLISECONDS);
    }
}
