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
    VALUES (1, '2020-11-15 15:30:14.332', 0, 1, 1),
           (2, '2020-11-15 15:30:14.332', 1, 1, 1),
           (3, '2020-11-15 15:30:14.332', 1, 1, 1),
           (4, '2020-11-15 15:30:14.332', 1, 2, 1),
           (5, '2020-11-15 15:30:14.332', 0, 1, 1),
           (6, '2020-11-15 15:30:14.332', 1, 5, 1),
           (7, '2020-11-15 15:30:14.332', 1, 1, 1),
           (8, '2020-11-15 15:30:14.332', 0, 6, 1),
           (9, '2020-11-15 15:30:14.332', 1, 10, 1),
           (10, '2020-11-15 15:30:14.332', 0, 2, 1),
           (11, '2020-11-15 15:30:14.332', 1, 4, 1),
           (12, '2020-11-15 15:30:14.332', 1, 9, 1);
