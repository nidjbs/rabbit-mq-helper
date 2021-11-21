package com.hyl.mq.helper.producer;


import com.hyl.mq.helper.cache.MqRetryCounter;
import com.hyl.mq.helper.common.AppInfoHolder;
import com.hyl.mq.helper.common.CompensateState;
import com.hyl.mq.helper.common.TreadPoolStrategy;
import com.hyl.mq.helper.config.MqHelperConfig;
import com.hyl.mq.helper.consumer.SingleSpringBeanWrapper;
import com.hyl.mq.helper.exx.FrameworkException;
import com.hyl.mq.helper.producer.compensate.IMqSendFailLogMapper;
import com.hyl.mq.helper.producer.compensate.MqSendFailLogDO;
import com.hyl.mq.helper.util.JsonUtil;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author huayuanlin
 * @date 2021/09/22 14:35
 * @desc Please guarantee rabbitmq.publisher-confirms=true
 */
public abstract class AbstractConfirmedMqSender implements RabbitTemplate.ConfirmCallback, InitializingBean, RabbitMqSender, BeanNameAware {

    private static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();

    protected static ThreadPoolExecutor THREAD_POOL_EXECUTOR = new
            ThreadPoolExecutor(CORE_COUNT, CORE_COUNT * 10,
            2000, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(2000),
            new CustomizableThreadFactory("mq-sender-retry-pool"),
            TreadPoolStrategy.runsOldestTaskPolicy());

    static {
        // Allow the core thread to be destroyed when there is no task after timeout.
        THREAD_POOL_EXECUTOR.allowCoreThreadTimeOut(true);
    }

    protected String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public String getBeanName() {
        return beanName;
    }

    protected MqRetryCounter mqRetryCounter = MqRetryCounter.DEFAULT;

    private final SingleSpringBeanWrapper<IMqSendFailLogMapper> sendFailLogMapper = new SingleSpringBeanWrapper<IMqSendFailLogMapper>() {
    };

    @Value("${spring.application.name}")
    private String appName;

    private static final String DEFAULT_APP_NAME = "default_app_name";
    private static final String MSG_PRE = "mq-helper:send-confirmed:";

    @Resource
    protected MqHelperConfig config;

    @Autowired(required = false)
    protected RabbitTemplate rabbitTemplate;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            mqRetryCounter.remove(correlationData.getId());
        } else {
            ConfirmedMsgWrapper message = mqRetryCounter.get(correlationData.getId());
            if (message == null) {
                return;
            }
            Integer already = message.getRetryTimes();
            if (already >= config.getProducer().getRetryTimes()) {
                if (isRecordWhenFail()) {
                    addFailLog(message);
                }
                return;
            }
            // async retry
            retryAsync(correlationData, message);
        }
    }

    private void retryAsync(CorrelationData correlationData, ConfirmedMsgWrapper message) {
        THREAD_POOL_EXECUTOR.submit(() -> {
            doSend(message.getExchange(), message.getRoutingKey(), message.getMsg(), correlationData);
            message.setRetryTimes(message.getRetryTimes() + 1);
            mqRetryCounter.put(correlationData.getId(), message);
        });
    }

    @Override
    public void send(String exchange, String routingKey, String msg) {
        String uuid =  MSG_PRE + UUID.randomUUID().toString();
        ConfirmedMsgWrapper message = new ConfirmedMsgWrapper(msg, exchange);
        message.setRetryTimes(0);
        message.setRoutingKey(routingKey);
        mqRetryCounter.put(uuid, message);
        CorrelationData correlationData = new CorrelationData(uuid);
        doSend(exchange, routingKey, msg, correlationData);
    }

    protected void doSend(String exchange, String routingKey, String msg, CorrelationData correlationData) {
        rabbitTemplate().convertAndSend(exchange, routingKey, msg, correlationData);
    }

    /**
     * rabbitTemplate
     * If your project contains multiple Rabbit Templates (without priority distinction)：
     * please override this method to specify your Rabbit Template, otherwise an error will be reported.
     *
     * @return rabbitTemplate
     */
    protected RabbitTemplate rabbitTemplate() {
        if (this.rabbitTemplate != null) {
            return this.rabbitTemplate;
        }
        throw new FrameworkException("check there is only one rabbit Template in your spring container, " +
                "or you rewrite the rabbitTemplate() method of the sender!");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RabbitTemplate rabbitTemplate = rabbitTemplate();
        // Only one ConfirmCallback is supported by each RabbitTemplate
        if (!rabbitTemplate.isConfirmListener()) {
            rabbitTemplate.setConfirmCallback(this);
        }
    }

    /**
     * When the maximum number of retries still fails, whether to record the sent message.
     *
     * @return result
     */
    protected boolean isRecordWhenFail() {
        return false;
    }

    private void addFailLog(ConfirmedMsgWrapper message) {
        MqSendFailLogDO mqSendFailLog = new MqSendFailLogDO();
        String appName = AppInfoHolder.APP_INFO.getAppName();
        mqSendFailLog.setAppName(appName);
        mqSendFailLog.setSendBeanName(beanName);
        mqSendFailLog.setRetry(message.getRetryTimes());
        mqSendFailLog.setCreateTime(System.currentTimeMillis());
        mqSendFailLog.setUpdateTime(System.currentTimeMillis());
        mqSendFailLog.setMsgInfo(JsonUtil.toJsonString(message));
        mqSendFailLog.setState(CompensateState.WAIT_COMPENSATE.getId());
        sendFailLogMapper.getBean().addFailLog(mqSendFailLog);
    }
}
