package com.hyl.mq.helper.consumer;


import java.util.Date;

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
     * 状态，1代表已补偿完毕，0代表初始状态，注意0不代表消费失败！
     */
    private Boolean status;

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
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Override
    public String toString() {
        return "ConsumerLogDO{" +
                "id=" + id +
                ", uniqueId='" + uniqueId + '\'' +
                ", status=" + status +
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
