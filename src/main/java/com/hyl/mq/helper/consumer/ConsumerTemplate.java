package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.exx.UnexpectedException;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

import java.io.IOException;

/**
 * @author huayuanlin
 * @date 2021/10/24 16:27
 * @desc the class desc
 */
public abstract class ConsumerTemplate {


    protected static final Logger LOGGER = LoggerFactory.getLogger(ConsumerTemplate.class);


    protected final SingleSpringBeanWrapper<IMqLogMapper> mqLogMapper = new SingleSpringBeanWrapper<IMqLogMapper>(){};

    public final Object consumer(InvocationConsumerParam consumerParam) {
        Object result = null;
        ConsumerParamHolder paramHolder = (ConsumerParamHolder) consumerParam;
        if (!preCheck(paramHolder)) {
            onPreCheckFail(paramHolder);
            return null;
        }
        try {
            result = consumerParam.invokeConsumerLogic();
            basicAck(paramHolder);
        } catch (Throwable consumerException) {
            LOGGER.error("consumer msg fail,msg id:" + paramHolder.getMsgUid(), consumerException);
            onConsumerFail(paramHolder);
        }
        return result;
    }

    protected void basicAck(ConsumerParamHolder paramHolder) {
        Channel channel = paramHolder.getChannel();
        Message message = paramHolder.getMessage();
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException ioException) {
            LOGGER.error("basicAck fail", ioException);
            throw new UnexpectedException("basicAck fail", ioException);
        }
    }


    protected void basicNack(ConsumerParamHolder paramHolder) {
        Channel channel = paramHolder.getChannel();
        Message message = paramHolder.getMessage();
        try {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        } catch (IOException ioException) {
            LOGGER.error("basicNack fail", ioException);
            throw new UnexpectedException("basicNack fail", ioException);
        }
    }

    /**
     * on consumer pre check
     *
     * @param consumerParam consumerParam
     * @return result
     */
    protected boolean preCheck(ConsumerParamHolder consumerParam) {
        return true;
    }

    /**
     * consumer pre check fail
     *
     * @param consumerParam consumerParam
     */
    protected void onPreCheckFail(ConsumerParamHolder consumerParam) {

    }

    /**
     * on consumer complete
     *
     * @param consumerParam consumerParam
     */
    protected void onConsumerComplete(ConsumerParamHolder consumerParam) {

    }

    /**
     * on consumer fail
     *
     * @param consumerParam consumerParam
     */
    protected abstract void onConsumerFail(ConsumerParamHolder consumerParam);
}
