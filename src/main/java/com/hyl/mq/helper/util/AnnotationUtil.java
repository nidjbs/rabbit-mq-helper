package com.hyl.mq.helper.util;

import com.hyl.mq.helper.annotation.HpRabbitListener;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author huayuanlin
 * @date 2021/10/20 12:59
 * @desc the class desc
 */
public class AnnotationUtil {


    private AnnotationUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * 解析队列名称
     *
     * @param hpRabbitListener hpRabbitListener
     * @return 队列名称
     */
    public static Set<String> resolveQueueNames(HpRabbitListener hpRabbitListener) {
        Set<String> queueNames = new HashSet<>();
        hpRabbitListener.bindings();
        if (hpRabbitListener.bindings().length > 0) {
            QueueBinding[] bindings = hpRabbitListener.bindings();
            Set<String> names1 = StreamUtil.nonNull(bindings)
                    .map(queueBinding -> queueBinding.value().name())
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toSet());
            Set<String> names2 = StreamUtil.nonNull(bindings)
                    .map(queueBinding -> queueBinding.value().value())
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toSet());
            queueNames.addAll(names1);
            queueNames.addAll(names2);
        }
        if (hpRabbitListener.queues().length > 0) {
            Set<String> names = StreamUtil.nonNull(hpRabbitListener.queues())
                    .filter(StringUtils::hasText).collect(Collectors.toSet());
            queueNames.addAll(names);
        }
        if (hpRabbitListener.queuesToDeclare().length > 0) {
            Set<String> names1 = StreamUtil.nonNull(hpRabbitListener.queuesToDeclare())
                    .map(Queue::value).collect(Collectors.toSet());
            Set<String> names2 = StreamUtil.nonNull(hpRabbitListener.queuesToDeclare())
                    .map(Queue::name).collect(Collectors.toSet());
            queueNames.addAll(names1);
            queueNames.addAll(names2);
        }
        if (CollectionUtils.isEmpty(queueNames)) {
            throw new RuntimeException("queue Name not found from hpRabbitListener annotation!");
        }
        return queueNames;
    }

}
