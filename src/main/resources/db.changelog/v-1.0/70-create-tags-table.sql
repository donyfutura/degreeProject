CREATE TABLE `tags` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

GO

INSERT INTO `tags` (`id`, `name`)
    VALUES (1, 'Spring'),
           (2, 'Java'),
           (3, 'SQL'),
           (4, 'Hibernate'),
           (5, 'Test');
