package com.hyl.mq.helper.producer;

/**
 * @author huayuanlin
 * @date 2021/10/21 20:47
 * @desc the class desc
 */
public class ConfirmedMsgWrapper {

    private String msg;

    private String exchange;

    private String routingKey;

    private Integer retryTimes;

    public ConfirmedMsgWrapper(String msg, String exchange) {
        this.msg = msg;
        this.exchange = exchange;
    }

    public ConfirmedMsgWrapper() {

    }

    public String getMsg() {
        return msg;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getExchange() {
        return exchange;
    }
}
