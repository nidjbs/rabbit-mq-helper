package com.hyl.mq.helper.producer.compensate;

import com.hyl.mq.helper.common.CompensateState;
import com.hyl.mq.helper.producer.AbstractConfirmedMqSender;
import com.hyl.mq.helper.producer.ConfirmedMsgWrapper;
import com.hyl.mq.helper.util.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author huayuanlin
 * @date 2021/11/19 11:46
 * @desc the class desc
 */
public class LocalMqSenderFailCompensator extends AbstractSendFailCompensator {

    public static final LocalMqSenderFailCompensator INSTANCE = new LocalMqSenderFailCompensator();

    private LocalMqSenderFailCompensator() {

    }

    /**
     * get sender by bean name
     *
     * @param beanName beanName
     * @return sender bean,null able
     */
    protected static Optional<AbstractConfirmedMqSender> getSenderByBeanName(String beanName) {
        if (StringUtils.isEmpty(beanName)) {
            return Optional.empty();
        }
        Map<String, AbstractConfirmedMqSender> beansOfType = SpringBeanUtil.getApplicationContext().getBeansOfType(AbstractConfirmedMqSender.class);
        if (CollectionUtils.isEmpty(beansOfType)) {
            return Optional.empty();
        }
        return Optional.ofNullable(beansOfType.get(beanName));
    }


    @Override
    public void process(List<MqSendFailLogDO> items) {
        if(CollectionUtils.isEmpty(items)) {
            return;
        }
        StreamUtil.nonNull(items).forEach(item -> {
            Optional<AbstractConfirmedMqSender> senderByBeanName = getSenderByBeanName(item.getSendBeanName());
            senderByBeanName.ifPresent(sender -> {
                // process with tx
                ManualTxParams<Void> manualTxParams = new ManualTxParams<>();
                manualTxParams.setTimeout(-1);
                manualTxParams.setFunc(param -> singletonProcess(sender, item));
                ManualTxUtil.startTxWithOutParams(manualTxParams);
            });
        });
    }

    private Void singletonProcess(AbstractConfirmedMqSender sender, MqSendFailLogDO mqSendFailLog) {
        String msgInfo = mqSendFailLog.getMsgInfo();
        // update log to Compensating state
        boolean dbLockSucceed = sendFailLogMapper.getBean().updateState(mqSendFailLog.getId(),
                CompensateState.WAIT_COMPENSATE, CompensateState.COMPLETING);
        if (dbLockSucceed) {
            ConfirmedMsgWrapper confirmedMsgWrapper = JsonUtil.fromJson(msgInfo, ConfirmedMsgWrapper.class);
            sender.send(confirmedMsgWrapper.getExchange(),
                    confirmedMsgWrapper.getRoutingKey(), confirmedMsgWrapper.getMsg());
            // update log to Compensate complete state
            sendFailLogMapper.getBean().updateState(mqSendFailLog.getId(),
                    CompensateState.COMPLETING, CompensateState.COMPLETE);
        }
        return null;
    }


}
