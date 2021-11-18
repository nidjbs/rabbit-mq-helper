package com.hyl.mq.helper.support;

import com.hyl.mq.helper.common.JdbcTemplateHolder;
import com.hyl.mq.helper.util.RedisUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author huayuanlin
 * @date 2021/10/26 09:50
 * @desc Inject a custom bean to the current framework, and the don't injected properties will use the default bean by default
 * @see JdbcTemplateHolder
 * @see RedisUtil
 */
public class MqHelperManager implements InitializingBean {

    private RedisTemplate<String, Object> redisTemplate;

    private JdbcTemplate jdbcTemplate;

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (redisTemplate != null) {
            RedisUtil.initRedisTemplate(getRedisTemplate());
        }
        if (jdbcTemplate != null) {
            JdbcTemplateHolder.initJdbcTemplate(getJdbcTemplate());
        }
    }
}
