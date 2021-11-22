package com.hyl.mq.helper.common;

import com.hyl.mq.helper.util.SpringBeanUtil;
import org.springframework.util.StringUtils;

/**
 * @author huayuanlin
 * @date 2021/11/21 21:27
 * @desc the class desc
 */
public class AppInfoHolder {

    public static final AppInfoHolder APP_INFO = new AppInfoHolder();

    private String appName;

    public String getAppName() {
        if (StringUtils.hasText(appName)) {
            return this.appName;
        } else {
            return SpringBeanUtil.springApplicationName();
        }
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    private AppInfoHolder() {

    }


}
