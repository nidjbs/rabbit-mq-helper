package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.annotation.HpRabbitListener;
import com.hyl.mq.helper.util.ManualTxParams;
import com.hyl.mq.helper.util.ManualTxUtil;
import org.springframework.lang.Nullable;

/**
 * @author huayuanlin
 * @date 2021/10/24 14:38
 * @desc the class desc
 */
public class SmartConsumerExecutor implements ConsumerExecutor<InvocationConsumerParam> {

    public static final ConsumerExecutor<InvocationConsumerParam> INSTANCE = new SmartConsumerExecutor();

    @Override
    public Object doExecute(InvocationConsumerParam paramHolder) {
        ConsumerParamHolder consumerParamHolder = (ConsumerParamHolder) paramHolder;
        ConsumerTemplate template = obtainTemplate(consumerParamHolder);
        boolean wrapTx = consumerParamHolder.isWrapTx();
        if (wrapTx) {
            ManualTxParams<Object> manualTxParams = new ManualTxParams<>();
            manualTxParams.setFunc(param -> doConsumer(template, consumerParamHolder));
            manualTxParams.setTimeout(consumerParamHolder.getTxTimeOut());
            return ManualTxUtil.startTxWithOutParams(manualTxParams);
        }
        return doConsumer(template, consumerParamHolder);
    }

    private Object doConsumer(ConsumerTemplate template, ConsumerParamHolder paramHolder) {
        return template == null ? paramHolder.invokeConsumerLogic() : template.consumer(paramHolder);
    }

    @Nullable
    protected ConsumerTemplate obtainTemplate(ConsumerParamHolder paramHolder) {
        boolean openIdempotent = paramHolder.isOpenIdempotent();
        if (openIdempotent) {
            HpRabbitListener.IDEMPOTENT idempotentType = paramHolder.getIdempotentType();
            if (idempotentType == HpRabbitListener.IDEMPOTENT.DB) {
                return DbIdempotentConsumerTemplate.INSTANCE;
            } else {
                return RedisIdempotentConsumerTemplate.INSTANCE;
            }
        }
        boolean retry = paramHolder.isRetry();
        if (retry) {
            return SimpleRetryConsumerTemplate.INSTANCE;
        }
        return null;
    }
}
