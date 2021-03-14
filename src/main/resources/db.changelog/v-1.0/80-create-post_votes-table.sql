CREATE TABLE `post_votes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` datetime NOT NULL,
  `value` bit(1) NOT NULL,
  `post_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9jh5u17tmu1g7xnlxa77ilo3u` (`post_id`),
  KEY `FK9q09ho9p8fmo6rcysnci8rocc` (`user_id`),
  CONSTRAINT `FK9jh5u17tmu1g7xnlxa77ilo3u` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK9q09ho9p8fmo6rcysnci8rocc` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8

GO

INSERT INTO `post_votes` (`id`, `time`, `value`, `post_id`, `user_id`)
    VALUES (1, '2017-11-15 15:30:14.332', 1, 1, 1);
