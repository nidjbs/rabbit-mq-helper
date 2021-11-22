package com.hyl.mq.helper.annotation;

import com.hyl.mq.helper.consumer.HpRabbitConsumerBeanPostProcessor;
import com.hyl.mq.helper.protocol.BaseMqMsg;
import com.hyl.mq.helper.support.MqHelperManager;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;

import java.lang.annotation.*;

/**
 * @author huayuanlin
 * @date 2021/09/15 16:28
 * @desc The reason for not using @AliasFor is that RabbitListenerAnnotationBeanPostProcessor does not use AnnotatedElementUtils to obtain annotation attributes, so using @AliasFor will not take effect
 * @see RabbitListenerAnnotationBeanPostProcessor#postProcessAfterInitialization
 * @see HpRabbitConsumerBeanPostProcessor
 * Before using the retry or idempotent function, make sure that your message body contains a unique id
 * @see BaseMqMsg Please inherit your message object from this base class, or you can customize the unique Id, and ensure that the field names are consistent
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RabbitListener
@Inherited
public @interface HpRabbitListener {

    /**
     * @see RabbitListener#id()
     */
    String id() default "";

    /**
     * @see RabbitListener#bindings()
     */
    QueueBinding[] bindings() default {};

    /**
     * @see RabbitListener#containerFactory()
     */
    String containerFactory() default "";

    /**
     * @see RabbitListener#group()
     */
    String group() default "";

    /**
     * @see RabbitListener#queues()
     */
    String[] queues() default {};

    /**
     * @see RabbitListener#queuesToDeclare()
     */
    Queue[] queuesToDeclare() default {};

    /**
     * @see RabbitListener#exclusive()
     */
    boolean exclusive() default false;

    /**
     * @see RabbitListener#priority()
     */
    String priority() default "";

    /**
     * @see RabbitListener#admin()
     */
    String admin() default "";

    /**
     * @see RabbitListener#returnExceptions()
     */
    String returnExceptions() default "";

    /**
     * @see RabbitListener#errorHandler()
     */
    String errorHandler() default "";

    /**
     * @see RabbitListener#concurrency()
     */
    String concurrency() default "";

    /**
     * @see RabbitListener#autoStartup()
     */
    String autoStartup() default "";

    /**
     * @see RabbitListener#executor()
     */
    String executor() default "";

    /**
     * @see RabbitListener#ackMode()
     */
    String ackMode() default "";

    /**
     * @see RabbitListener#replyPostProcessor()
     */
    String replyPostProcessor() default "";

    /**
     * When an error occurs, the number of retries, 0 or a negative number means no retry,
     * please pay attention to distinguish it from the retry mechanism that comes with spring-mq;
     * It does not take effect when idempotence is turned on.
     *
     * @return times
     */
    int retryTimes() default -1;

    /**
     * Retry sleep time (ms), 0 or a negative number means no sleep, it is recommended to turn on when idempotence is not required
     *If you do not sleep, it may cause a quick retry.
     * It does not take effect when idempotence is turned on.
     *
     * @return ms
     */
    long retrySleepTimeOut() default 0L;

    /**
     * Open idempotence
     *
     * @return Open idempotence?
     */
    boolean openIdempotent() default false;

    /**
     * Idempotent implementation type Effective when idempotence is turned on
     * Redis: Realized by redis, weak consistency, higher concurrency.
     * @return Idempotent type
     */
    IDEMPOTENT idempotentType() default IDEMPOTENT.REDIS;

    /**
     * Whether to perform the package transaction for consumption.
     * If you are performing a local transaction (that is, no rpc operation), and you need strong consistency, you can choose to enable.
     * If the business operation contains many rpc operations, it is of little significance to enable it. It is not recommended to enable it unless you perform distributed transaction processing on the entire operation.
     * @return Whether to perform wrap tx
     */
    boolean wrapTx() default false;

    /**
     * wrapTx tx timeout  unit：s
     * @return  wrapTx tx timeout
     */
    int txTimeOut() default 5;


    enum IDEMPOTENT {
        /**
         * by db unique key，concurrent degree is lower.
         * Please ensure that you are have a date Source in spring environment.
         * custom set dataSource:
         * @see MqHelperManager
         */
        DB,
        /**
         * by redis set commend，concurrent degree is higher.
         * Please ensure that you are have a redis template in spring environment.
         * custom set redis template:
         * @see MqHelperManager
         */
        REDIS,
    }


}
