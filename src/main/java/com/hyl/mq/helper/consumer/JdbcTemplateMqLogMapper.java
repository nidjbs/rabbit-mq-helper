package com.hyl.mq.helper.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

/**
 * @author huayuanlin
 * @date 2021/10/24 17:55
 * @desc the class desc
 */
public class JdbcTemplateMqLogMapper implements IMqLogMapper {

    private static final String SELECT_ID_BY_UID_SQL = "SELECT id,`unique_id` AS uniqueId, status, retry, message, create_time AS createTime,update_time AS updateTime FROM `mq_log` WHERE unique_id = '%s' ";
    private static final String INSERT_SQL = "INSERT INTO `mq_log` (`unique_id`,`status`,`retry`,`message`,`create_time`,`update_time`) VALUES (?,?,?,?,?,?) ";
    private static final String UPDATE_RETRY_TIMES_SQL = "UPDATE `mq_log` SET `retry` = `retry` + 1 WHERE `unique_id` = ? ";

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public int addRetryTimes(String mqLogUid, String msg) {
        MqLogDO mqLogDO = obtainOrAdd(mqLogUid, msg);
        jdbcTemplate.update(UPDATE_RETRY_TIMES_SQL, mqLogUid);
        return mqLogDO.getRetry() + 1;
    }

    @Override
    public boolean tryAddMqLog(String mqLogUid,String msg) {
        try {
            addNewLog(mqLogUid, msg);
        } catch (DuplicateKeyException e) {
            return false;
        }
        return true;
    }


    private MqLogDO obtainOrAdd(String mqLogUid, String msg) {
        String sqlFormat = String.format(SELECT_ID_BY_UID_SQL, mqLogUid);
        List<MqLogDO> oldMqLog = jdbcTemplate.query(sqlFormat, new BeanPropertyRowMapper<>(MqLogDO.class));
        MqLogDO result;
        if (CollectionUtils.isEmpty(oldMqLog)) {
            result = addNewLog(mqLogUid, msg);
        } else {
            result = oldMqLog.get(0);
        }
        return result;
    }

    @Override
    public void addMqLog(String mqLogUid, String msg) {
        obtainOrAdd(mqLogUid, msg);
    }

    private MqLogDO addNewLog(String mqLogUid, String msg) {
        MqLogDO mqLogDO = new MqLogDO();
        mqLogDO.setCreateTime(new Date());
        mqLogDO.setRetry(0);
        mqLogDO.setStatus(Boolean.FALSE);
        mqLogDO.setUpdateTime(new Date());
        mqLogDO.setUniqueId(mqLogUid);
        mqLogDO.setMessage(msg);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    INSERT_SQL, new String[]{"id"});
            preparedStatement.setString(1, mqLogDO.getUniqueId());
            preparedStatement.setBoolean(2, mqLogDO.getStatus());
            preparedStatement.setInt(3, mqLogDO.getRetry());
            preparedStatement.setString(4, mqLogDO.getMessage());
            preparedStatement.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            preparedStatement.setDate(6, new java.sql.Date(System.currentTimeMillis()));
            return preparedStatement;
        }, keyHolder);
        return mqLogDO;
    }

}
