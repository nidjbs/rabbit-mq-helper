package com.hyl.mq.helper.config;

import com.hyl.mq.helper.consumer.*;
import com.hyl.mq.helper.consumer.compensate.ConsumerFailLocalTimerCompensator;
import com.hyl.mq.helper.consumer.compensate.CustomerCompensatorScanner;
import com.hyl.mq.helper.producer.compensate.IMqSendFailLogMapper;
import com.hyl.mq.helper.producer.compensate.JdbcTemplateMqSendFailLogMapper;
import com.hyl.mq.helper.producer.compensate.MqSendFailLocalTimerCompensator;
import com.hyl.mq.helper.util.SpringBeanUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author huayuanlin
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MqHelperConfig.class)
public class MqHelperAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public HpRabbitConsumerBeanPostProcessor hpRabbitConsumerBeanPostProcessor() {
        return new HpRabbitConsumerBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public AbstractAopProxyCreator hpRabbitListenerAopProxyCreator() {
        return new HpRabbitListenerAopProxyCreator(new HpRabbitListenerMethodInterceptor());
    }

    @Bean
    @ConditionalOnMissingBean
    public IMqConsumerLogMapper jdbcTemplateMqConsumerLogMapper() {
        return new JdbcTemplateMqConsumerLogMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public IMqSendFailLogMapper jdbcTemplateMqSendFailLogMapper() {
        return new JdbcTemplateMqSendFailLogMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringBeanUtil hpSpringBeanUtil() {
        return new SpringBeanUtil();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mq.helper.compensator", value = "localSenderEnable", havingValue = "true")
    public MqSendFailLocalTimerCompensator mqSendFailLocalTimerCompensator() {
        return new MqSendFailLocalTimerCompensator();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mq.helper.compensator", value = "localConsumerEnable", havingValue = "true")
    public CustomerCompensatorScanner customerCompensatorScanner() {
        return new CustomerCompensatorScanner();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mq.helper.compensator", value = "localConsumerEnable", havingValue = "true")
    public ConsumerFailLocalTimerCompensator consumerFailLocalTimerCompensator() {
        return new ConsumerFailLocalTimerCompensator();
    }






}
