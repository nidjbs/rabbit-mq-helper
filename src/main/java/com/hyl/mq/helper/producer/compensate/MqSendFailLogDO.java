package com.hyl.mq.helper.producer.compensate;

/**
 * @author huayuanlin
 * @date 2021/11/18 20:35
 * @desc the class desc
 */
public class MqSendFailLogDO {

    /**
     * primaryKey
     */
    private Long id;

    /**
     * app name
     */
    private String appName;

    /**
     * sender bean name
     */
    private String sendBeanName;

    /**
     * retry
     */
    private Integer retry;

    /**
     * message info
     */
    private String msgInfo;

    /**
     * state (1-to be compensated, 2-compensation completed, -1 others)
     */
    private Integer state;

    /**
     * createTime
     */
    private Long createTime;

    /**
     * updateTime
     */
    private Long updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public String getMsgInfo() {
        return msgInfo;
    }

    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
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

    public String getSendBeanName() {
        return sendBeanName;
    }

    public void setSendBeanName(String sendBeanName) {
        this.sendBeanName = sendBeanName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MqSendFailLogDO{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", sendBeanName='" + sendBeanName + '\'' +
                ", retry=" + retry +
                ", msgInfo='" + msgInfo + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
