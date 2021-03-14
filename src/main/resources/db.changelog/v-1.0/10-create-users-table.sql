CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `reg_time` datetime NOT NULL,
  `email` varchar(255) NOT NULL,
  `is_moderator` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `photo` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

GO

INSERT INTO `users` (`id`, `code`, `reg_time`, `email`, `is_moderator`, `name`, `password`, `photo`)
    VALUES (1, null, '2017-11-15 15:30:14.332', 'test1@mail.com', 1, 'test1', 'test', null),
           (2, null, '2017-11-15 15:30:14.332', 'test2@mail.com', 0, 'test2', 'test', null);