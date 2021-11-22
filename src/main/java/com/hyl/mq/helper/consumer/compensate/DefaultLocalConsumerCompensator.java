package com.hyl.mq.helper.consumer.compensate;

import com.hyl.mq.helper.common.CompensateState;
import com.hyl.mq.helper.config.MqHelperConfig;
import com.hyl.mq.helper.consumer.MqConsumerHolderContext;
import com.hyl.mq.helper.consumer.MqConsumerLogDO;
import com.hyl.mq.helper.consumer.SingleSpringBeanWrapper;
import com.hyl.mq.helper.util.ManualTxParams;
import com.hyl.mq.helper.util.ManualTxUtil;
import com.hyl.mq.helper.util.StreamUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huayuanlin
 * @date 2021/11/22 11:08
 * @desc the class desc
 */
public class DefaultLocalConsumerCompensator extends AbstractConsumerCompensator {

    public static final DefaultLocalConsumerCompensator INSTANCE = new DefaultLocalConsumerCompensator();


    protected final SingleSpringBeanWrapper<MqHelperConfig> mqConfig = new SingleSpringBeanWrapper<MqHelperConfig>() {
    };

    @Override
    public void process(List<MqConsumerLogDO> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        if (CollectionUtils.isEmpty(getLocalConsumerQueueNames())) {
            return;
        }
        StreamUtil.nonNull(items).forEach(item -> {
            // process with tx
            ManualTxParams<Void> manualTxParams = new ManualTxParams<>();
            manualTxParams.setTimeout(-1);
            manualTxParams.setFunc(param -> singletonProcess(item));
            ManualTxUtil.startTxWithOutParams(manualTxParams);
        });
    }

    private Void singletonProcess(MqConsumerLogDO mqConsumerLog) {
        String consumerQueueNames = mqConsumerLog.getConsumerQueueNames();
        if (StringUtils.isEmpty(consumerQueueNames)) {
            return null;
        }
        List<String> consumerQueueNameList = StreamUtil.slipWithColon(consumerQueueNames, Function.identity());
        List<String> localConsumerQueueNames = getLocalConsumerQueueNames();
        List<String> filterLocalQueueNames = StreamUtil.nonNull(consumerQueueNameList)
                .filter(localConsumerQueueNames::contains).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterLocalQueueNames)) {
            return null;
        }
        // update log to Compensating state
        boolean dbLockSucceed = mqConsumerLogMapper.getBean().tryUpdateState(mqConsumerLog.getId(),
                CompensateState.WAIT_COMPENSATE, CompensateState.COMPLETING);
        if (dbLockSucceed) {
            String message = mqConsumerLog.getMessage();
            MqConsumerHolderContext.invokeSingleConsumer(filterLocalQueueNames, message);
            // update log to Compensate complete state
            mqConsumerLogMapper.getBean().tryUpdateState(mqConsumerLog.getId(),
                    CompensateState.COMPLETING, CompensateState.COMPLETE);
        }
        return null;
    }


    private List<String> getLocalConsumerQueueNames() {
        MqHelperConfig.Compensator compensator = mqConfig.getBean().getCompensator();
        return compensator.getLocalConsumerQueueNames();
    }
}
