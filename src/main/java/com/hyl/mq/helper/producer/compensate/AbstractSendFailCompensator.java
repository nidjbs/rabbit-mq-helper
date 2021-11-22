package com.hyl.mq.helper.producer.compensate;

import com.hyl.mq.helper.common.AppInfoHolder;
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
        String appName = AppInfoHolder.APP_INFO.getAppName();
        return sendFailLogMapper.getBean().listCompensateLog(appName, batchSize());
    }


    @Override
    public int batchSize() {
        return 50;
    }
}
