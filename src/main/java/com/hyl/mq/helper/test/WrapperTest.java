package com.hyl.mq.helper.test;

import com.hyl.mq.helper.consumer.IMqLogMapper;
import com.hyl.mq.helper.consumer.SingleSpringBeanWrapper;

/**
 * @author huayuanlin
 * @date 2021/11/05 00:52
 * @desc the class desc
 */
public class WrapperTest {

    protected static final SingleSpringBeanWrapper<IMqLogMapper> mqLogMapper = new SingleSpringBeanWrapper<IMqLogMapper>(){};

    public static void main(String[] args) {
        IMqLogMapper mqLogMapperBean = mqLogMapper.getBean();
    }


}
