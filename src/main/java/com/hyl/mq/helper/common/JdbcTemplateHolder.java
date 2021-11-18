package com.hyl.mq.helper.common;

import com.hyl.mq.helper.util.SpringBeanUtil;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author huayuanlin
 * @date 2021/11/18 20:42
 * @desc the class desc
 */
public class JdbcTemplateHolder {

    private volatile static JdbcTemplate jdbcTemplate;

    /**
     * get jdbcTemplate
     * get it from spring when it is empty
     *
     * @return redisTemplate
     */
    public static JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            synchronized (JdbcTemplateHolder.class) {
                if (jdbcTemplate == null) {
                    jdbcTemplate = SpringBeanUtil.getBeanByType(JdbcTemplate.class);
                }
            }
        }
        return jdbcTemplate;
    }

    /**
     * init jdbcTemplate
     *
     * @param jdbcTemplate jdbcTemplate
     */
    public static void initJdbcTemplate(JdbcTemplate jdbcTemplate) {
        JdbcTemplateHolder.jdbcTemplate = jdbcTemplate;
    }

}
