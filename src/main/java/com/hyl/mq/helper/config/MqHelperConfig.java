package com.hyl.mq.helper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huayuanlin
 * @date 2021/10/22 14:15
 * @desc the class desc
 */
@ConfigurationProperties(prefix = "mq.helper")
public class MqHelperConfig {


    private Producer producer;

    private Consumer consumer;


    public static class Producer{
        private Integer retryTimes = 3;

        public Integer getRetryTimes() {
            return retryTimes;
        }

        public void setRetryTimes(Integer retryTimes) {
            this.retryTimes = retryTimes;
        }
    }



    public static class Consumer{

    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
}
