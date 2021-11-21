package com.hyl.mq.helper.producer.compensate;

import com.hyl.mq.helper.common.BatchCompensator;
import com.hyl.mq.helper.consumer.SingleSpringBeanWrapper;

import java.util.List;

/**
 * @author huayuanlin
 * @date 2021/11/19 15:05
 * @desc the class desc
 */
public abstract class AbstractSendFailCompensator implements BatchCompensator<MqSendFailLogDO> {

    protected final SingleSpringBeanWrapper<IMqSendFailLogMapper> sendFailLogMapper = new SingleSpringBeanWrapper<IMqSendFailLogMapper>() {
    };

    @Override
    public List<MqSendFailLogDO> getItems(int batchSize) {
        return sendFailLogMapper.getBean().listCompensateLog(batchSize());
    }

    @Override
    public void doCompensate() {
        List<MqSendFailLogDO> items = getItems(batchSize());
        process(items);
    }


    @Override
    public int batchSize() {
        return 50;
    }
}