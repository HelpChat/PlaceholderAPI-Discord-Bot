CREATE TABLE `papibot_servers` (
  `id` SMALLINT(6) NOT NULL AUTO_INCREMENT,
  `guild_id` BIGINT(20) NULL,
  `prefix` TEXT NULL,
  PRIMARY KEY (`id`)
) COLLATE = 'utf8_general_ci' ENGINE = MyISAM;