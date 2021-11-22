package com.hyl.mq.helper.consumer.compensate;

import com.hyl.mq.helper.common.AppInfoHolder;
import com.hyl.mq.helper.common.BatchCompensator;
import com.hyl.mq.helper.consumer.IMqConsumerLogMapper;
import com.hyl.mq.helper.consumer.MqConsumerLogDO;
import com.hyl.mq.helper.consumer.SingleSpringBeanWrapper;

import java.util.List;

/**
 * @author huayuanlin
 * @date 2021/11/22 10:39
 * @desc the class desc
 */
public abstract class AbstractConsumerCompensator implements BatchCompensator<MqConsumerLogDO> {

    protected final SingleSpringBeanWrapper<IMqConsumerLogMapper> mqConsumerLogMapper = new SingleSpringBeanWrapper<IMqConsumerLogMapper>() {
    };

    @Override
    public List<MqConsumerLogDO> getItems(int batchSize) {
        String appName = AppInfoHolder.APP_INFO.getAppName();
        return mqConsumerLogMapper.getBean().listMqConsumerLog(batchSize, appName);
    }


    @Override
    public int batchSize() {
        return 50;
    }


}
