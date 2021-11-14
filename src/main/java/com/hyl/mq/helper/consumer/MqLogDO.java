package com.hyl.mq.helper.consumer;


/**
 * @author huayuanlin
 * @date 2021/08/07 20:43
 * @desc the class desc
 */
public class MqLogDO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 消息唯一id
     */
    private String uniqueId;

    /**
     * 消费队列名
     */
    private String consumerQueueNames;

    /**
     * 重试次数
     */
    private Integer retry;

    /**
     * 消息
     */
    private String message;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
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
