CREATE TABLE `mq_log` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'primaryKey',
  `unique_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'messageUniqueIds',
  `retry` int(10) DEFAULT NULL COMMENT 'number of retries',
  `consumer_queue_names` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'consumer queue names',
  `message` varchar(2048) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'message',
  `create_time` bigint(20) DEFAULT NULL COMMENT 'created time(ms)',
  `update_time` bigint(20) DEFAULT NULL COMMENT 'recently updated time(ms)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_1` (`unique_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;