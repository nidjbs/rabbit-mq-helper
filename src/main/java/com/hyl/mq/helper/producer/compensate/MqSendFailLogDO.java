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
     * retry
     */
    private Integer retry;

    /**
     * message info
     */
    private String msgInfo;

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

    @Override
    public String toString() {
        return "MqSendFailLogDO{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", retry=" + retry +
                ", msgInfo='" + msgInfo + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
