package com.hyl.mq.helper.config;

import com.hyl.mq.helper.consumer.*;
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
    public MethodInterceptor hpRabbitListenerMethodInterceptor() {
        return new HpRabbitListenerMethodInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public HpRabbitConsumerBeanPostProcessor hpRabbitConsumerBeanPostProcessor() {
        return new HpRabbitConsumerBeanPostProcessor();
    }

    @Bean
    public AbstractAopProxyCreator  hpRabbitListenerAopProxyCreator() {
        return new HpRabbitListenerAopProxyCreator(hpRabbitListenerMethodInterceptor());
    }

    @Bean
    @ConditionalOnMissingBean
    public JdbcTemplateMqLogMapper jdbcTemplateMqLogMapper() {
        return new JdbcTemplateMqLogMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringBeanUtil hpSpringBeanUtil() {
        return new SpringBeanUtil();
    }
}
