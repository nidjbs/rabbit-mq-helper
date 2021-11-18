package com.hyl.mq.helper.config;

import com.hyl.mq.helper.consumer.*;
import com.hyl.mq.helper.producer.compensate.IMqSendFailLogMapper;
import com.hyl.mq.helper.producer.compensate.JdbcTemplateMqSendFailLogMapper;
import com.hyl.mq.helper.util.SpringBeanUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author huayuanlin
 */
@Configuration
@EnableConfigurationProperties(MqHelperConfig.class)
public class MqHelperAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public MethodInterceptor hpRabbitListenerMethodInterceptor() {
        return new HpRabbitListenerMethodInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public HpRabbitConsumerBeanPostProcessor hpRabbitConsumerBeanPostProcessor() {
        return new HpRabbitConsumerBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public AbstractAopProxyCreator hpRabbitListenerAopProxyCreator() {
        return new HpRabbitListenerAopProxyCreator(hpRabbitListenerMethodInterceptor());
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
}
