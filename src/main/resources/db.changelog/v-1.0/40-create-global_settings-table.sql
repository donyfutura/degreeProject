CREATE TABLE `global_settings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` char(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

GO

INSERT INTO `global_settings` (`id`, `code`, `name`, `value`)
    VALUES ('1', 'MULTIUSER_MODE', 'Многопользовательский режим', '0'),
            ('2', 'POST_PREMODERATION ', 'Премодерация постов ', '0'),
            ('3', 'STATISTICS_IS_PUBLIC ', 'Показывать всем статистику блога', '0');