package com.hyl.mq.helper.consumer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import spock.lang.Specification

@SpringBootTest(classes = Config)
class JdbcTemplateMqLogMapperTest extends Specification {

    @Autowired
    JdbcTemplateMqConsumerLogMapper jdbcTemplateMqLogMapper

    def "test1"() {
        when:
        def result = jdbcTemplateMqLogMapper.tryAddMqLog("123456")
        println("测试")
        then:
        !result
    }

    @Configuration
    @Import([DataSourceAutoConfiguration,JdbcTemplateMqConsumerLogMapper, JdbcTemplateAutoConfiguration])
    @Profile("test")
    static class Config {



    }

}
