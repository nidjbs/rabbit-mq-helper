package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.annotation.HpRabbitListener;
import com.hyl.mq.helper.util.JsonUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * @author huayuanlin
 * @date 2021/10/24 15:22
 * @desc Consumer Param Holder
 */
public class ConsumerParamHolder extends InvocationConsumerParam {

    private Channel channel;

    private Message message;

    private Object msgPayload;

    private String msgUid;

    private boolean isRetry;

    private int retryTimes;

    private long retrySleepTimeOut;

    private boolean openIdempotent;

    private HpRabbitListener.IDEMPOTENT idempotentType;

    private boolean wrapTx;

    private int txTimeOut;


    public boolean isWrapTx() {
        return wrapTx;
    }

    public void setWrapTx(boolean wrapTx) {
        this.wrapTx = wrapTx;
    }

    public boolean isRetry() {
        return isRetry;
    }

    public void setRetry(boolean retry) {
        isRetry = retry;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getTxTimeOut() {
        return txTimeOut;
    }

    public void setTxTimeOut(int txTimeOut) {
        this.txTimeOut = txTimeOut;
    }

    public long getRetrySleepTimeOut() {
        return retrySleepTimeOut;
    }

    public String getMsgUid() {
        return msgUid;
    }

    public void setMsgUid(String msgUid) {
        this.msgUid = msgUid;
    }

    public void setRetrySleepTimeOut(long retrySleepTimeOut) {
        this.retrySleepTimeOut = retrySleepTimeOut;
    }

    public boolean isOpenIdempotent() {
        return openIdempotent;
    }

    public void setOpenIdempotent(boolean openIdempotent) {
        this.openIdempotent = openIdempotent;
    }

    public HpRabbitListener.IDEMPOTENT getIdempotentType() {
        return idempotentType;
    }

    public void setIdempotentType(HpRabbitListener.IDEMPOTENT idempotentType) {
        this.idempotentType = idempotentType;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Object getMsgPayload() {
        return msgPayload;
    }

    public String getMsgPayloadStr() {
        if (this.msgPayload instanceof String) {
            return (String) this.msgPayload;
        } else {
            return JsonUtil.toJsonString(this.msgPayload);
        }
    }

    public void setMsgPayload(Object msgPayload) {
        this.msgPayload = msgPayload;
    }
}
