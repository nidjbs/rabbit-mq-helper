package com.hyl.mq.helper.protocol;

import java.util.UUID;

/**
 * @author huayuanlin
 * @date 2021/10/21 20:24
 * @desc the class desc
 */
public abstract class BaseMqMsg {

    private final String uniqueId;

    public BaseMqMsg() {
        this.uniqueId = UUID.randomUUID().toString();
    }

    public BaseMqMsg(String uniqueId) {
        this.uniqueId = uniqueId;
    }


    public String getUniqueId() {
        return uniqueId;
    }
}
