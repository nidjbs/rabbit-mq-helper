package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.common.AppInfoHolder;
import com.hyl.mq.helper.common.CompensateState;
import com.hyl.mq.helper.common.JdbcTemplateHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Set;

/**
 * @author huayuanlin
 * @date 2021/10/24 17:55
 * @desc the class desc
 */
public class JdbcTemplateMqConsumerLogMapper implements IMqConsumerLogMapper {

    private static final String BASE_SELECT_SQL = "SELECT id,`unique_id` AS uniqueId, state, retry, message, create_time AS createTime,update_time AS updateTime,consumer_queue_names AS consumerQueueNames,`app_name` AS appName FROM `mq_consumer_log` WHERE ";
    private static final String SELECT_ID_BY_UID_SQL = BASE_SELECT_SQL + "unique_id = '%s' ";
    private static final String SELECT_ID_BY_APP_NAME_SQL = BASE_SELECT_SQL + "`app_name` = '%s' AND `state` = 1 LIMIT %d ";
    private static final String INSERT_SQL = "INSERT INTO `mq_consumer_log` (`unique_id`,`retry`,`message`,`create_time`,`update_time`,`consumer_queue_names`,`state`,`app_name`) VALUES (?,?,?,?,?,?,?,?) ";
    private static final String UPDATE_RETRY_TIMES_SQL = "UPDATE `mq_consumer_log` SET `retry` = `retry` + 1, `update_time` = current_timestamp() WHERE `unique_id` = ? ";
    private static final String UPDATE_STATE_SQL = "UPDATE `mq_consumer_log` SET `state` = ?, `update_time` = current_timestamp() WHERE `unique_id` = ? ";
    private static final String TRY_UPDATE_STATE_SQL = "UPDATE `mq_consumer_log` SET `state` = ?, `update_time` = current_timestamp() WHERE `id` = ? and `state` = ? ";


    @Override
    public int addRetryTimes(String mqLogUid, String msg, Set<String> consumerQueueNames) {
        String consumerQueueNamesStr = null;
        if (!CollectionUtils.isEmpty(consumerQueueNames)) {
            consumerQueueNamesStr = String.join(",", consumerQueueNames);
        }
        MqConsumerLogDO mqLogDO = obtainOrAdd(mqLogUid, msg, consumerQueueNamesStr, null);
        JdbcTemplateHolder.getJdbcTemplate().update(UPDATE_RETRY_TIMES_SQL, mqLogUid);
        return mqLogDO.getRetry() + 1;
    }

    @Override
    public boolean tryAddMqLog(String mqLogUid, String msg) {
        try {
            addNewLog(mqLogUid, msg, null, null);
        } catch (DuplicateKeyException e) {
            return false;
        }
        return true;
    }


    private MqConsumerLogDO obtainOrAdd(String mqLogUid, String msg, String consumerQueueNames, CompensateState compensateState) {
        String sqlFormat = String.format(SELECT_ID_BY_UID_SQL, mqLogUid);
        List<MqConsumerLogDO> oldMqLog = JdbcTemplateHolder.getJdbcTemplate()
                .query(sqlFormat, new BeanPropertyRowMapper<>(MqConsumerLogDO.class));
        MqConsumerLogDO result;
        if (CollectionUtils.isEmpty(oldMqLog)) {
            result = addNewLog(mqLogUid, msg, consumerQueueNames, compensateState);
        } else {
            result = oldMqLog.get(0);
        }
        return result;
    }

    @Override
    public void addMqLog(String mqLogUid, String msg, Set<String> consumerQueueNames, CompensateState compensateState) {
        String consumerQueueNamesStr = null;
        if (!CollectionUtils.isEmpty(consumerQueueNames)) {
            consumerQueueNamesStr = String.join(",", consumerQueueNames);
        }
        obtainOrAdd(mqLogUid, msg, consumerQueueNamesStr, compensateState);
    }

    @Override
    public void updateState(String mqLogUid, CompensateState compensateState) {
        Assert.notNull(compensateState, "compensateState enum is null");
        Assert.notNull(mqLogUid, "mqLogUid is null");
        JdbcTemplateHolder.getJdbcTemplate().update(UPDATE_STATE_SQL, compensateState.getId(), mqLogUid);
    }

    @Override
    public boolean tryUpdateState(Long id, CompensateState source, CompensateState compensateState) {
        Assert.notNull(id, "id is null");
        Assert.notNull(source, "source CompensateState enum is null");
        Assert.notNull(compensateState, "target compensateState enum is null");
        return JdbcTemplateHolder.getJdbcTemplate().update(TRY_UPDATE_STATE_SQL, compensateState.getId(), id, source.getId()) > 0;
    }

    @Override
    public List<MqConsumerLogDO> listMqConsumerLog(int batchSize, String appName) {
        String sqlFormat = String.format(SELECT_ID_BY_APP_NAME_SQL, appName, batchSize);
        return JdbcTemplateHolder.getJdbcTemplate()
                .query(sqlFormat, new BeanPropertyRowMapper<>(MqConsumerLogDO.class));
    }


    private MqConsumerLogDO addNewLog(String mqLogUid, String msg, String consumerQueueNames, CompensateState compensateState) {
        MqConsumerLogDO mqLogDO = new MqConsumerLogDO();
        mqLogDO.setCreateTime(System.currentTimeMillis());
        mqLogDO.setRetry(0);
        mqLogDO.setUpdateTime(System.currentTimeMillis());
        mqLogDO.setUniqueId(mqLogUid);
        mqLogDO.setMessage(msg);
        mqLogDO.setConsumerQueueNames(consumerQueueNames);
        int compensateStateId = compensateState == null ? CompensateState.OTHER.getId() : compensateState.getId();
        mqLogDO.setState(compensateStateId);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String appName = AppInfoHolder.APP_INFO.getAppName();
        mqLogDO.setAppName(appName);
        JdbcTemplateHolder.getJdbcTemplate().update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    INSERT_SQL, new String[]{"id"});
            preparedStatement.setString(1, mqLogDO.getUniqueId());
            preparedStatement.setInt(2, mqLogDO.getRetry());
            preparedStatement.setString(3, mqLogDO.getMessage());
            preparedStatement.setLong(4, mqLogDO.getCreateTime());
            preparedStatement.setLong(5, mqLogDO.getUpdateTime());
            preparedStatement.setString(6, mqLogDO.getConsumerQueueNames());
            preparedStatement.setInt(7, mqLogDO.getState());
            preparedStatement.setString(8, mqLogDO.getAppName());
            return preparedStatement;
        }, keyHolder);
        return mqLogDO;
    }

}
