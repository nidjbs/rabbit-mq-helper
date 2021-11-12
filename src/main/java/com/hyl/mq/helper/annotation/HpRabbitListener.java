package com.hyl.mq.helper.annotation;

import com.hyl.mq.helper.consumer.HpRabbitConsumerBeanPostProcessor;
import com.hyl.mq.helper.protocol.BaseMqMsg;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.*;

/**
 * @author huayuanlin
 * @date 2021/09/15 16:28
 * @desc 不使用@AliasFor的原因是RabbitListenerAnnotationBeanPostProcessor 中并没有使用AnnotatedElementUtils来获取注解属性所以用@AliasFor不会生效
 * @see RabbitListenerAnnotationBeanPostProcessor#postProcessAfterInitialization
 * @see HpRabbitConsumerBeanPostProcessor
 * @see AnnotatedElementUtils
 * 使用重试或幂等功能前，需保证您的消息体中含有唯一id
 * @see BaseMqMsg 请将您的消息对象继承该基类，或您自定义uniqueId，需保证字段名一致
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
     * 当发生错误时，重试的次数，0或负数代表不重试，请注意与spring-mq自带的重试机制区分开；
     * 开启幂等时不生效。
     *
     * @return times
     */
    int retryTimes() default -1;

    /**
     * 重试休眠时间（ms），0或负数代表不休眠，无需幂等时建议开启
     * 若不休眠，可能会导致快速重试。
     * 开启幂等时不生效。
     *
     * @return ms
     */
    long retrySleepTimeOut() default 0L;

    /**
     * 开启幂等
     *
     * @return 是否开启幂等
     */
    boolean openIdempotent() default false;

    /**
     * 幂等实现类型 开启幂等时有效
     * DB：通过数据库的事务及唯一key实现，强一致性，并发度更低；请保证处于有dateSource环境
     * Redis: 通过redis实现，弱一致性，并发度更高.
     * @return 幂等类型
     */
    IDEMPOTENT idempotentType() default IDEMPOTENT.DB;

    /**
     * 执行是否包裹事务进行消费
     * 如果你执行的是本地事务（即无rpc操作），且需要强一致性，可选择开启
     * 如果业务操作含有多数rpc操作，则开启的意义不大，除非你将整个操作进行了分布式事务处理，否则不建议开启。
     * @return 执行是否包裹事务
     */
    boolean wrapTx() default false;

    /**
     * wrapTx 事务超时时间  单位：s
     * @return  事务超时时间
     */
    int txTimeOut() default 5;


    enum IDEMPOTENT {
        /**
         * 幂等实现类型
         */
        DB,
        REDIS,
    }


}
