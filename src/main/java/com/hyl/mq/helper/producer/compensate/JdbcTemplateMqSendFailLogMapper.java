package com.hyl.mq.helper.producer.compensate;

import com.hyl.mq.helper.common.JdbcTemplateHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;

/**
 * @author huayuanlin
 * @date 2021/11/18 20:41
 * @desc the class desc
 */
public class JdbcTemplateMqSendFailLogMapper implements IMqSendFailLogMapper {

    private static final String INSERT_SQL = "INSERT INTO `mq_send_fail_log`(`app_name`,`retry`,`msg_info`,`create_time`,`update_time`) VALUES(?,?,?,?,?)";

    @Override
    public void addFailLog(MqSendFailLogDO mqSendFailLog) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplateHolder.getJdbcTemplate().update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    INSERT_SQL, new String[]{"id"});
            preparedStatement.setString(1, mqSendFailLog.getAppName());
            preparedStatement.setInt(2, mqSendFailLog.getRetry());
            preparedStatement.setString(3, mqSendFailLog.getMsgInfo());
            preparedStatement.setLong(4, mqSendFailLog.getCreateTime());
            preparedStatement.setLong(5, mqSendFailLog.getUpdateTime());
            return preparedStatement;
        }, keyHolder);
    }
}
