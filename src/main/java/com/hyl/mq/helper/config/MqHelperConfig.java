package com.hyl.mq.helper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huayuanlin
 * @date 2021/10/22 14:15
 * @desc the class desc
 */
@ConfigurationProperties(prefix = "mq.helper")
public class MqHelperConfig {


    private Producer producer = new Producer();

    private Consumer consumer = new Consumer();

    private Compensator compensator = new Compensator();


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

        /*** one day ms*/
        private static final long  DEFAULT_REDIS_IDEMPOTENT_LOCK_TTL = 86400000L;

        private Long redisIdempotentLockTtl = DEFAULT_REDIS_IDEMPOTENT_LOCK_TTL;

        public Long getRedisIdempotentLockTtl() {
            return redisIdempotentLockTtl;
        }

        public void setRedisIdempotentLockTtl(Long redisIdempotentLockTtl) {
            this.redisIdempotentLockTtl = redisIdempotentLockTtl;
        }
    }


    public static class Compensator{

        private Boolean localSenderEnable;

        public Boolean getLocalSenderEnable() {
            return localSenderEnable;
        }

        public void setLocalSenderEnable(Boolean localSenderEnable) {
            this.localSenderEnable = localSenderEnable;
        }
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


    public Compensator getCompensator() {
        return compensator;
    }

    public void setCompensator(Compensator compensator) {
        this.compensator = compensator;
    }
}
