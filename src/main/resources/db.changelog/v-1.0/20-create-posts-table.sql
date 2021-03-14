CREATE TABLE `posts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` datetime NOT NULL,
  `is_active` bit(1) NOT NULL,
  `moderator_id` int DEFAULT NULL,
  `moderation_status` varchar(255) NOT NULL,
  `text` longtext NOT NULL,
  `title` varchar(255) NOT NULL,
  `view_count` int NOT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5lidm6cqbc7u4xhqpxm898qme` (`user_id`),
  CONSTRAINT `FK5lidm6cqbc7u4xhqpxm898qme` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8

GO
INSERT INTO `posts` (`id`, `time`, `is_active`, `moderator_id`, `moderation_status`, `text`, `title`, `view_count`, `user_id`)
    VALUES ('1', '2017-11-15 15:30:14.332', 1, 1, 'ACCEPTED', 'testText', 'testTitle', 1, 2);
