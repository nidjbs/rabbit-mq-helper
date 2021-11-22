package com.hyl.mq.helper.producer.compensate;

import com.hyl.mq.helper.common.CompensateState;
import com.hyl.mq.helper.common.JdbcTemplateHolder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * @author huayuanlin
 * @date 2021/11/18 20:41
 * @desc the class desc
 */
public class JdbcTemplateMqSendFailLogMapper implements IMqSendFailLogMapper {

    private static final String INSERT_SQL = "INSERT INTO `mq_send_fail_log`(`app_name`,`retry`,`msg_info`,`create_time`,`update_time`,`send_bean_name`,`state`) VALUES(?,?,?,?,?,?,?)";

    private static final String SELECT_SQL = "SELECT `id`,`retry`,`app_name` AS appName,`msg_info` AS msgInfo,`state`,`send_bean_name` AS sendBeanName,`create_time` AS createTime,`update_time` FROM `mq_send_fail_log` WHERE `state` = 1 AND `app_name` = '%s'  LIMIT %d ";
    private static final String UPDATE_STATE_SQL = "UPDATE `mq_send_fail_log` SET `state` = ? WHERE `id` = ? AND `state` = ? ";

    @Override
    public void addFailLog(MqSendFailLogDO mqSendFailLog) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplateHolder.getJdbcTemplate().update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            preparedStatement.setString(1, mqSendFailLog.getAppName());
            preparedStatement.setInt(2, mqSendFailLog.getRetry());
            preparedStatement.setString(3, mqSendFailLog.getMsgInfo());
            preparedStatement.setLong(4, mqSendFailLog.getCreateTime());
            preparedStatement.setLong(5, mqSendFailLog.getUpdateTime());
            preparedStatement.setString(6, mqSendFailLog.getSendBeanName());
            preparedStatement.setInt(7, mqSendFailLog.getState());
            return preparedStatement;
        }, keyHolder);
    }

    @Override
    public List<MqSendFailLogDO> listCompensateLog(String appName, int batchSize) {
        Assert.hasLength(appName,"app name is empty!");
        String sqlFormat = String.format(SELECT_SQL, appName, batchSize);
        return JdbcTemplateHolder.getJdbcTemplate()
                .query(sqlFormat, new BeanPropertyRowMapper<>(MqSendFailLogDO.class));
    }

    @Override
    public boolean updateState(Long id, CompensateState original, CompensateState compensateState) {
        return JdbcTemplateHolder.getJdbcTemplate().update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATE_SQL);
            preparedStatement.setInt(1, compensateState.getId());
            preparedStatement.setLong(2, id);
            preparedStatement.setInt(3, original.getId());
            return preparedStatement;
        }) > 0;
    }
}
