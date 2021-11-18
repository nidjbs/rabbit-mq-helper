CREATE TABLE `mq_consumer_log` (
  `id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `unique_id` VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'message unique id',
  `retry` INT(10) DEFAULT NULL COMMENT 'number of retries',
  `consumer_queue_names` VARCHAR(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'consumer queue names',
  `message` TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'message',
  `create_time` BIGINT(20) DEFAULT NULL COMMENT 'created time(ms)',
  `update_time` BIGINT(20) DEFAULT NULL COMMENT 'recently updated time(ms)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_1` (`unique_id`)
) ENGINE=INNODB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `mq_send_fail_log` (
  `id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `app_name` VARCHAR(256) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'sender app name',
  `retry` INT(10) DEFAULT NULL COMMENT 'number of retries',
  `msg_info` TEXT DEFAULT NULL COMMENT 'message info',
  `create_time` BIGINT(20) DEFAULT NULL COMMENT 'created time(ms)',
  `update_time` BIGINT(20) DEFAULT NULL COMMENT 'recently updated time(ms)',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;