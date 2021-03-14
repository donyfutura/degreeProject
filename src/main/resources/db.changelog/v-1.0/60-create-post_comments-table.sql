CREATE TABLE `post_comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` datetime NOT NULL,
  `parent_id` int DEFAULT NULL,
  `text` longtext NOT NULL,
  `post_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKaawaqxjs3br8dw5v90w7uu514` (`post_id`),
  KEY `FKsnxoecngu89u3fh4wdrgf0f2g` (`user_id`),
  CONSTRAINT `FKaawaqxjs3br8dw5v90w7uu514` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKsnxoecngu89u3fh4wdrgf0f2g` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8

GO

INSERT INTO `post_comments` (`id`, `time`, `parent_id`, `text`, `post_id`, `user_id`)
    VALUES (1, '2017-11-15 15:30:14.332', null, 'testComment', 1, 1);
