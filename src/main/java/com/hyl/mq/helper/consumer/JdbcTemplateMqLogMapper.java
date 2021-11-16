package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.util.SpringBeanUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Set;

/**
 * @author huayuanlin
 * @date 2021/10/24 17:55
 * @desc the class desc
 */
public class JdbcTemplateMqLogMapper implements IMqLogMapper {

    private static final String SELECT_ID_BY_UID_SQL = "SELECT id,`unique_id` AS uniqueId, status, retry, message, create_time AS createTime,update_time AS updateTime,consumer_queue_names AS consumerQueueNames FROM `mq_log` WHERE unique_id = '%s' ";
    private static final String INSERT_SQL = "INSERT INTO `mq_log` (`unique_id`,`retry`,`message`,`create_time`,`update_time`,`consumer_queue_names`) VALUES (?,?,?,?,?,?) ";
    private static final String UPDATE_RETRY_TIMES_SQL = "UPDATE `mq_log` SET `retry` = `retry` + 1, `update_time` = current_timestamp() WHERE `unique_id` = ? ";

    private volatile static JdbcTemplate jdbcTemplate;

    /**
     * get JdbcTemplate
     * Get it from spring when it is empty
     *
     * @return redisTemplate
     */
    public static JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            synchronized (JdbcTemplateMqLogMapper.class) {
                if (jdbcTemplate == null) {
                    jdbcTemplate = SpringBeanUtil.getBeanByType(JdbcTemplate.class);
                }
            }
        }
        return jdbcTemplate;
    }

    public static void initJdbcTemplate(JdbcTemplate jdbcTemplate) {
        JdbcTemplateMqLogMapper.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int addRetryTimes(String mqLogUid, String msg, Set<String> consumerQueueNames) {
        String consumerQueueNamesStr = null;
        if (!CollectionUtils.isEmpty(consumerQueueNames)) {
            consumerQueueNamesStr = consumerQueueNames.toString();
        }
        MqLogDO mqLogDO = obtainOrAdd(mqLogUid, msg, consumerQueueNamesStr);
        getJdbcTemplate().update(UPDATE_RETRY_TIMES_SQL, mqLogUid);
        return mqLogDO.getRetry() + 1;
    }

    @Override
    public boolean tryAddMqLog(String mqLogUid, String msg) {
        try {
            addNewLog(mqLogUid, msg, null);
        } catch (DuplicateKeyException e) {
            return false;
        }
        return true;
    }


    private MqLogDO obtainOrAdd(String mqLogUid, String msg, String consumerQueueNames) {
        String sqlFormat = String.format(SELECT_ID_BY_UID_SQL, mqLogUid);
        List<MqLogDO> oldMqLog = getJdbcTemplate().query(sqlFormat, new BeanPropertyRowMapper<>(MqLogDO.class));
        MqLogDO result;
        if (CollectionUtils.isEmpty(oldMqLog)) {
            result = addNewLog(mqLogUid, msg, consumerQueueNames);
        } else {
            result = oldMqLog.get(0);
        }
        return result;
    }

    @Override
    public void addMqLog(String mqLogUid, String msg, Set<String> consumerQueueNames) {
        String consumerQueueNamesStr = null;
        if (!CollectionUtils.isEmpty(consumerQueueNames)) {
            consumerQueueNamesStr = consumerQueueNames.toString();
        }
        obtainOrAdd(mqLogUid, msg, consumerQueueNamesStr);
    }

    private MqLogDO addNewLog(String mqLogUid, String msg, String consumerQueueNames) {
        MqLogDO mqLogDO = new MqLogDO();
        mqLogDO.setCreateTime(System.currentTimeMillis());
        mqLogDO.setRetry(0);
        mqLogDO.setUpdateTime(System.currentTimeMillis());
        mqLogDO.setUniqueId(mqLogUid);
        mqLogDO.setMessage(msg);
        mqLogDO.setConsumerQueueNames(consumerQueueNames);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    INSERT_SQL, new String[]{"id"});
            preparedStatement.setString(1, mqLogDO.getUniqueId());
            preparedStatement.setInt(2, mqLogDO.getRetry());
            preparedStatement.setString(3, mqLogDO.getMessage());
            preparedStatement.setLong(4, mqLogDO.getCreateTime());
            preparedStatement.setLong(5, mqLogDO.getUpdateTime());
            preparedStatement.setString(6, mqLogDO.getConsumerQueueNames());
            return preparedStatement;
        }, keyHolder);
        return mqLogDO;
    }

}
