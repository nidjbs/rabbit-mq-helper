package com.hyl.mq.helper.consumer;


/**
 * @author huayuanlin
 * @date 2021/08/07 20:43
 * @desc the class desc
 */
public class MqLogDO {

    /**
     * primaryKey
     */
    private Long id;

    /**
     * messageUniqueId
     */
    private String uniqueId;

    /**
     * consumer queue name
     */
    private String consumerQueueNames;

    /**
     * numberOfRetries
     */
    private Integer retry;

    /**
     * message
     */
    private String message;

    /**
     * createTime
     */
    private Long createTime;

    /**
     * updateTime
     */
    private Long updateTime;

    @Override
    public String toString() {
        return "MqLogDO{" +
                "id=" + id +
                ", uniqueId='" + uniqueId + '\'' +
                ", consumerQueueNames='" + consumerQueueNames + '\'' +
                ", retry=" + retry +
                ", message='" + message + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getConsumerQueueNames() {
        return consumerQueueNames;
    }

    public void setConsumerQueueNames(String consumerQueueNames) {
        this.consumerQueueNames = consumerQueueNames;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
