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
    VALUES (1, '2020-11-15 15:30:14.332', null, 'testComment', 1, 1),
           (2, '2020-11-15 15:30:14.332', null, 'testComment', 2, 1),
           (3, '2020-11-15 15:30:14.332', null, 'testComment', 5, 1),
           (4, '2020-11-15 15:30:14.332', null, 'testComment', 5, 1),
           (5, '2020-11-15 15:30:14.332', null, 'testComment', 9, 1),
           (6, '2020-11-15 15:30:14.332', null, 'testComment', 1, 1),
           (7, '2020-11-15 15:30:14.332', null, 'testComment', 6, 1),
           (8, '2020-11-15 15:30:14.332', null, 'testComment', 1, 1),
           (9, '2020-11-15 15:30:14.332', null, 'testComment', 7, 1),
           (10, '2020-11-15 15:30:14.332', null, 'testComment', 10, 1);
